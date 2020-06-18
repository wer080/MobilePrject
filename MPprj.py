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
areaCode = '1101'

#%%
def extract_total_count(url): # extract total number of data
    req = requests.get(url)
    html = req.text
    soup = BeautifulSoup(html, 'html.parser')
    totalCnt_list = soup.find_all('totalcnt')
    return totalCnt_list[0].text

def make_DB(date, DB):
    startIndex = 1
    endIndex = 1000  
    url = 'http://211.237.50.150:7080/openapi/' + key + '/xml/Grid_20141225000000000163_1/' + str(startIndex) +'/'+ str(endIndex)+'?EXAMIN_DE=' +str(date) + '&AREA_CD=' + areaCode
    totalCnt = int(extract_total_count(url))    
    
    list_examindate = []
    list_prdname = []
    list_unit = []
    #list_marketcd = []
    list_price = []
    list_species = []
    list_grade = []
    
    while startIndex <= totalCnt:
        url = 'http://211.237.50.150:7080/openapi/' + key + '/xml/Grid_20141225000000000163_1/' + str(startIndex) +'/'+ str(endIndex)+'?EXAMIN_DE=' + str(date) + '&AREA_CD=' + areaCode
        req = requests.get(url)
        html = req.text
        soup = BeautifulSoup(html, 'html.parser')
        
        examindateP = soup.find_all('examin_de')
        prdname = soup.find_all('prdlst_nm')
        unit = soup.find_all('examin_unit')
        price = soup.find_all('amt')
        species = soup.find_all('spcies_nm')
        grades = soup.find_all('grad_nm')
        
        for data in examindateP:
            list_examindate.append(data.text)
        for data in prdname:
            list_prdname.append(data.text)
        for data in unit:
            list_unit.append(data.text)
        for data in price:
            list_price.append(int(data.text))
        for data in species:
            list_species.append(data.text)
        for data in grades:
            list_grade.append(data.text)
            
        startIndex += 1000
        endIndex += 1000
        
    DB['product_name'] = list_prdname
    DB['species'] = list_species    
    DB['grade'] = list_grade
    DB['unit'] = list_unit
    DB['price'] = list_price
    DB['examin_date'] = list_examindate 
    return DB

def process_DB(DB):
    DB = DB.groupby(['product_name','species','grade','unit','examin_date'])
    DB = DB.mean()
    return DB

def cal_change_rate(todayDB, compareDB, nameIdx):
    changeNameIdx = ['vsYD', 'vsLM', 'vsLY', 'vsLW']
    calDF = pd.DataFrame()
    changeRateList = []
    name = []
    species = []
    grade = []
    if nameIdx == 0:
        for i in range(0, todayDB.shape[0]):
            for j in range(0, compareDB.shape[0]):
                if (todayDB.iloc[i].name[0] == compareDB.iloc[j].name[0]) and (todayDB.iloc[i].name[1] == compareDB.iloc[j].name[1]) and (todayDB.iloc[i].name[2] == compareDB.iloc[j].name[2]):
                    changeRate = (todayDB.iloc[i].price - compareDB.iloc[j].price) * 100 / compareDB.iloc[j].price
                    changeRateList.append(changeRate)
                    name.append(todayDB.iloc[i].name[0])
                    species.append(todayDB.iloc[i].name[1])
                    grade.append(todayDB.iloc[i].name[2])
            print(i)
    else:
        for i in range(0, todayDB.shape[0]):
            for j in range(0,compareDB.shape[0]):
                if(todayDB.loc[i,'product_name'] == compareDB.iloc[j].name[0]) and (todayDB.loc[i,'species'] == compareDB.iloc[j].name[1]) and (todayDB.loc[i,'grade'] == compareDB.iloc[j].name[2]):
                    changeRate = (todayDB.iloc[i].price - compareDB.iloc[j].price) * 100 / compareDB.iloc[j].price
                    changeRateList.append(changeRate)
                    name.append(todayDB.loc[i,'product_name'])
                    species.append(todayDB.loc[i,'species'])
                    grade.append(todayDB.loc[i,'grade'])
            print(i)
                        
    calDF['product_name'] = name
    calDF[changeNameIdx[nameIdx]] = changeRateList
    calDF['species'] = species
    calDF['grade'] = grade
    return calDF

#%%
#날짜 계산
today = datetime.date.today()
yesterday = today - timedelta(days=1)
lastweek = today - timedelta(days=7)
lastmonth = today - relativedelta(months=1)
lastyear = today - relativedelta(months=12)

#%%
todayDB = pd.DataFrame()
todayDB = make_DB(20200612, todayDB)
todayDB = process_DB(todayDB)
#%%
yesterdayDB = pd.DataFrame()
yesterdayDB = make_DB(20200611, yesterdayDB)
yesterdayDB = process_DB(yesterdayDB)
#%%
lastweekDB = pd.DataFrame()
lastweekDB = make_DB(20200604, lastweekDB)
lastweekDB = process_DB(lastweekDB)
#%%
lastmonthDB = pd.DataFrame()
lastmonthDB = make_DB(20200512, lastmonthDB)
lastmonthDB = process_DB(lastmonthDB)
#%%
lastyearDB = pd.DataFrame()
lastyearDB = make_DB(20190612, lastyearDB)
lastyearDB = process_DB(lastyearDB)
#%%
changeDF = cal_change_rate(todayDB, yesterdayDB, 0)
todayDB = todayDB.reset_index()
todayDB = pd.merge(todayDB,changeDF, how='left', on=['product_name', 'species', 'grade'])
#%%
changeDF2 = cal_change_rate(todayDB, lastmonthDB, 1)
todayDB = pd.merge(todayDB,changeDF2, how='left', on=['product_name', 'species', 'grade'])
#%%
changeDF3 = cal_change_rate(todayDB, lastyearDB, 2)
todayDB = pd.merge(todayDB,changeDF3, how='left', on=['product_name', 'species', 'grade'])
#%%
changeDF4 = cal_change_rate(todayDB, lastweekDB, 3)
todayDB = pd.merge(todayDB,changeDF4, how='left', on=['product_name', 'species', 'grade'])
#%%
todayDB = todayDB.sort_values(by='vsYD')
best_item_vsYD = todayDB.iloc[0:5,:]

todayDB = todayDB.sort_values(by='vsLW')
best_item_vsLW = todayDB.iloc[0:5,:]

todayDB = todayDB.sort_values(by='vsLM')
best_item_vsLM = todayDB.iloc[0:5,:]

todayDB = todayDB.sort_values(by='vsLY')
best_item_vsLY = todayDB.iloc[0:5,:]

#%%
todayDB.to_json('C:/Project/MP/database/todayDB.json', orient='table')
yesterdayDB.to_json('C:/Project/MP/database/yesterdayDB.json', orient='table')
lastweekDB.to_json('C:/Project/MP/database/lastweekDB.json', orient='table')
lastmonthDB.to_json('C:/Project/MP/database/lastmonthDB.json', orient='table')
lastyearDB.to_json('C:/Project/MP/database/lastyearDB.json', orient='table')
best_item_vsYD.to_json('C:/Project/MP/database/best_item_vsYD.json', orient='table')
best_item_vsLW.to_json('C:/Project/MP/database/best_item_vsLW.json', orient='table')
best_item_vsLM.to_json('C:/Project/MP/database/best_item_vsLM.json', orient='table')
best_item_vsLY.to_json('C:/Project/MP/database/best_item_vsLY.json', orient='table')


#%%
db_url = 'https://mpProject.firebaseio.com'

cred = credentials.Certificate("C:/Project/MP/mpproject-cd664-firebase-adminsdk-g23a7-1915f3d090.json")
firebase_admin.initialize_app(cred)

db = firestore.client()
#%%
with open('C:/Project/MP/database/todayDB.json') as json_file:
    json_data = json.load(json_file)

with open('C:/Project/MP/database/yesterdayDB.json') as json_file2:
    json_data2 = json.load(json_file2)
    
with open('C:/Project/MP/database/lastmonthDB.json') as json_file3:
    json_data3 = json.load(json_file3)
    
with open('C:/Project/MP/database/lastyearDB.json') as json_file4:
    json_data4 = json.load(json_file4)

with open('C:/Project/MP/database/lastweekDB.json') as json_file5:
    json_data5 = json.load(json_file5)
    
with open('C:/Project/MP/database/best_item_vsYD.json') as json_file6:
    json_data6 = json.load(json_file6)
    
with open('C:/Project/MP/database/best_item_vsLW.json') as json_file7:
    json_data7 = json.load(json_file7)
    
with open('C:/Project/MP/database/best_item_vsLM.json') as json_file8:
    json_data8 = json.load(json_file8)
    
with open('C:/Project/MP/database/best_item_vsLY.json') as json_file9:
    json_data9 = json.load(json_file9)

#%%

doc_ref = db.collection(u'today_price_info').document(u'today_price').set(json_data)
doc_ref = db.collection(u'yesterday_price_info').document(u'yd_price').set(json_data2)
doc_ref = db.collection(u'lastmonth_price_info').document(u'lm_price').set(json_data3)
doc_ref = db.collection(u'lastyear_price_info').document(u'ly_price').set(json_data4)
doc_ref = db.collection(u'lastweek_price_info').document(u'lw_price').set(json_data5)
doc_ref = db.collection(u'best_item_yesterday').document(u'yd_best').set(json_data6)
doc_ref = db.collection(u'best_item_lastweek').document(u'lw_best').set(json_data7)
doc_ref = db.collection(u'best_item_lastmont').document(u'lm_best').set(json_data8)
doc_ref = db.collection(u'best_item_lastyear').document(u'ly_best').set(json_data9)
