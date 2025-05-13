import pickle
import pandas as pd
from sklearn import metrics
from sklearn.naive_bayes import GaussianNB

#data_capture = pd.read_csv('scikit_test.arff')
#x = data_capture.drop('classe',axis=1)
#y = data_capture['classe']

loaded_model = pickle.load(open('NaiveBayesScikit.model', 'rb'))
#result = loaded_model.score(x, y)

#predictions = loaded_model.predict(x)

predictions = loaded_model.predict([[1,1.00000,1.00000,1.00000,1,1],[1,1.00000,1.00000,1.00000,1,1],[1,0.66667,0.63636,0.72727,5,1],[0,0.28571,0.27273,0.27273,7,3]])
prob = loaded_model.predict_proba([[1,1.00000,1.00000,1.00000,1,1],[1,1.00000,1.00000,1.00000,1,1],[1,0.66667,0.63636,0.72727,5,1],[0,0.28571,0.27273,0.27273,7,3]])
print predictions
print prob

#print "Accuracy:"+str(metrics.accuracy_score(y, predictions))
