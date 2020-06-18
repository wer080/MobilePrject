#%%
from bs4 import BeautifulSoup
import requests
import pandas as pd
import datetime
from datetime import timedelta
from dateutil.relativedelta import relativedelta
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json
#%%

key = 'e3513cb0ea92d9d81d7db2677c32f60325f60dfb91bf04d63d24f3890734064c'

#%%

def extract_total_count(url): # extract total number of data
    req = requests.get(url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    totalCnt_list = soup.find_all('totalcnt')
    return totalCnt_list[0].text

def db_for_process(DB):
    startIndex = 1
    endIndex = 1000
    url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000228_1/'+str(startIndex)+'/'+str(endIndex)
    totalCnt = int(extract_total_count(url))
    list_recipecode = []
    list_process = []
    list_processNum = []
    list_processImg = []
    
    while startIndex <= totalCnt:
        url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000228_1/'+str(startIndex)+'/'+str(endIndex)
        req = requests.get(url)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        
        soup_code = soup.find_all('recipe_id')
        soup_process = soup.find_all('cooking_dc')
        soup_processImg = soup.find_all('stre_step_image_url')
        soup_processNum = soup.find_all('cooking_no')
    
        for data in soup_code:
            list_recipecode.append(data.text)
            
        for data in soup_process:
            list_process.append(data.text)
            
        for data in soup_processImg:
            list_processImg.append(data.text)
            
        for data in soup_processNum:
            list_processNum.append(data.text)
            
        startIndex += 1000
        endIndex += 1000
        
    DB['recipe_code'] = list_recipecode
    DB['recipe_processNumber'] = list_processNum
    DB['recipe_process'] = list_process
    DB['recipe_processImg'] = list_processImg    
    return DB
    
def db_for_info(DB):   
    startIndex = 1
    endIndex = 1000
    url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000226_1/'+str(startIndex)+'/'+str(endIndex)
    list_recipecode = []
    list_Img = []
    list_name = []
    list_bInfo = []
    totalCnt = int(extract_total_count(url))
    
    while startIndex <= totalCnt:
        url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000226_1/'+str(startIndex)+'/'+str(endIndex)
        req = requests.get(url)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        
        soup_code = soup.find_all('recipe_id')
        soup_img = soup.find_all('img_url')
        soup_name = soup.find_all('recipe_nm_ko')
        soup_info = soup.find_all('sumry')
    
        for data in soup_code:
            list_recipecode.append(data.text)
            
        for data in soup_img:
            list_Img.append(data.text)
            
        for data in soup_name:
            list_name.append(data.text)
            
        for data in soup_info:
            list_bInfo.append(data.text)
            
        startIndex += 1000
        endIndex += 1000
        
    DB['recipe_code'] = list_recipecode
    DB['recipe_name'] = list_name
    DB['recipe_info'] = list_bInfo
    DB['recipe_img'] = list_Img
    
    return DB
    
def db_for_ingedient(DB):
    startIndex = 1
    endIndex = 1000
    url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000227_1/'+str(startIndex)+'/'+str(endIndex)
    totalCnt = int(extract_total_count(url))
    list_recipecode = []
    list_ing_name = []
    list_ing_amt = []
    
    while startIndex <= totalCnt:
        url = 'http://211.237.50.150:7080/openapi/'+key+'/xml/Grid_20150827000000000227_1/'+str(startIndex)+'/'+str(endIndex)
        req = requests.get(url)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        
        soup_code = soup.find_all('recipe_id')
        soup_ing_name = soup.find_all('irdnt_nm')
        soup_ing_amt = soup.find_all('irdnt_cpcty')
    
        for data in soup_code:
            list_recipecode.append(data.text)
            
        for data in soup_ing_name:
            list_ing_name.append(data.text)
            
        for data in soup_ing_amt:
            list_ing_amt.append(data.text)
            
        startIndex += 1000
        endIndex += 1000
        
    DB['recipe_code'] = list_recipecode
    DB['recipe_ing_name'] = list_ing_name
    DB['recipe_ing_amount'] = list_ing_amt 
    return DB
    

def upload_data(db):
    for i in range (0, recipe_Info.shape[0]):
            data2 = {
                u'recipe_code' : recipe_Info.recipe_code[i],
                u'recipe_name' : recipe_Info.recipe_name[i],
                u'recipe_info' : recipe_Info.recipe_info[i],
                u'recipe_img' : recipe_Info.recipe_img[i]
                }
            db.collection(u'recipes_info').document(str(i)).set(data2)
            
    for j in range (0, recipe_Process.shape[0]):
        data = {
            u'recipe_code' : recipe_Process.recipe_code[j],
            u'recipe_pr_num' : recipe_Process.recipe_processNumber[j],
            u'recipe_pr' : recipe_Process.recipe_process[j],
            u'recipe_pr_img' : recipe_Process.recipe_processImg[j]
            }            
        db.collection(u'recipes_pr').document(str(i)).set(data)
        
    for k in range (0, recipe_Ingredient.shape[0]):
        data3 = {
            u'recipe_code' : recipe_Ingredient.recipe_code[k],
            u'recipe_ing_name' : recipe_Ingredient.recipe_ing_name[k],
            u'recipe_ing_amount' : recipe_Ingredient.recipe_ing_amount[k]
            }
        db.collection(u'recipes_ing').document(str(i)).set(data3)
    
        
        
#%%
recipe_Info = pd.DataFrame()
recipe_Info = db_for_info(recipe_Info)

recipe_Process = pd.DataFrame()
recipe_Process = db_for_process(recipe_Process)

recipe_Ingredient = pd.DataFrame()
recipe_Ingredient = db_for_ingedient(recipe_Ingredient)
#%%
recipe_Info.to_json('C:/Project/MP/database/recipe_Info.json', orient='table')
recipe_Process.to_json('C:/Project/MP/database/recipe_Process.json', orient='table')
recipe_Ingredient.to_json('C:/Project/MP/database/recipe_Ingredient.json', orient='table')
#%%
db_url = 'https://mpProject.firebaseio.com'

cred = credentials.Certificate("C:/Project/MP/mpproject-cd664-firebase-adminsdk-g23a7-1915f3d090.json")
firebase_admin.initialize_app(cred)

db = firestore.client()
#%%
upload_data(db) 
    
    
    
    