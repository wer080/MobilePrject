#%%
#initialize parameters
#r_lambda: normalization parameter
#alpyha: confidence level
#nf: dimension of latent vector of each user and item
#initialized values(40, 200, 40) are the best parameters from the paper
r_lambda = 40
nf = 200
alpha = 40

#%%
import numpy as np
#train data set
#row = user(u), col = item(i)
R = np.array([[0,0,0,4,4,0,0,0,0,0,0],
              [0,0,0,0,0,0,0,0,0,0,1],
              [0,0,0,0,0,0,0,1,0,4,0],
              [0,3,4,0,3,0,0,2,2,0,0],
              [0,5,5,0,0,0,0,0,0,0,0],
              [0,0,0,0,0,0,5,0,0,5,0],
              [0,0,4,0,0,0,0,0,0,0,5],
              [0,0,0,0,0,4,0,0,0,0,4],
              [0,0,0,0,0,0,5,0,0,5,0],
              [0,0,0,3,0,0,0,0,4,5,0]])


#%%
#nu: number of user
#ni: number of item
#nf: dimension of latent vector
nu = R.shape[0]
ni = R.shape[1]

#initialize X and Y with very small values
X = np.random.rand(nu, nf) * 0.01
Y = np.random.rand(ni, nf) * 0.01

#%%
#convert train data set to binary rating matrix 
#if Rui > 0 then 1, otherwise 0
P = np.copy(R)
P[P>0] = 1
print(P)

#%%
#Initialize confidence matrix C
#Cui = 1 + alpha * Rui
#Cui means confidence level of certain rating data
C = 1 + alpha * R
print(C)
#%%
#Set up loss function
#C : confidence matrix
#P : binary rating matrix
#X : user latent matrix
#Y : item latent matrix
#r_lambda : regularization lambda
#xTy : predict Matrix
#Total_loss = (confidence_level * predict_loss) + regularization_loss

def loss_function(C, P, xTy, X, Y, r_lambda):
    predict_error = np.square(P - xTy)
    confidence_error = np.sum(C * predict_error)
    regularization = r_lambda * (np.sum(np.square(X)) + np.sum(np.square(Y)))
    total_loss = confidence_error + regularization
    return np.sum(predict_error), confidence_error, regularization, total_loss

#%%
#Optimization Function for user and item
#X[u] = (yTCuy + lambda*l)^-1yTCuy
#X[i] = (xTCix + lambda*l)^-1xTCuix
# two fomula is the same when it changes X to Y and u to i
def optimize_user(X, Y, C, P, nu, nf, r_lambda):
    yT = np.transpose(Y)
    for u in range(nu):
        Cu = np.diag(C[u])
        yT_Cu_y = np.matmul(np.matmul(yT, Cu), Y)
        lT = np.dot(r_lambda, np.identity(nf))
        yT_Cu_pu = np.matmul(np.matmul(yT, Cu), P[u])
        X[u] = np.linalg.solve(yT_Cu_y + lT, yT_Cu_pu)
        
def optimize_item(X, Y, C, P, ni, nf, r_lambda):
    xT = np.transpose(X)
    for i in range(ni):
        Ci = np.diag(C[:,i])
        xT_Ci_x = np.matmul(np.matmul(xT, Ci), X)
        lT = np.dot(r_lambda, np.identity(nf))
        xT_Ci_pi = np.matmul(np.matmul(xT, Ci), P[:,i])
        Y[i] = np.linalg.solve(xT_Ci_x + lT, xT_Ci_pi)
        
#%%
#Train
predict_errors = []
confidence_errors = []
regularization_list = []
total_losses = []

for i in range(30):
    if i != 0:
        optimize_user(X, Y, C, P, nu, nf, r_lambda)
        optimize_item(X, Y, C, P, ni, nf, r_lambda)
    
    predict = np.matmul(X, np.transpose(Y))
    predict_error, confidence_error, regularization, total_loss = loss_function(C, P, predict, X, Y, r_lambda)
    
    predict_errors.append(predict_error)
    confidence_errors.append(confidence_error)
    regularization_list.append(regularization)
    total_losses.append(total_loss)
    
    print('---------------------steop %d---------------------' %i)
    print("predict error: %f" % predict_error)
    print("confidence error: %f" % confidence_error)
    print("regularization : %f" % regularization)
    print("total loss: %f" % total_loss)
    
predict = np.matmul(X, np.transpose(Y))
print('final predict')
print([predict])

#%%
    
        
































