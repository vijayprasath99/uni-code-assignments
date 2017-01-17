# -*- coding: utf-8 -*-
"""
Created on Mon Nov 15 16:09:11 2016

@author: vijay
"""
import os
import time
import pandas as pd
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn import cross_validation
from sklearn.ensemble import RandomForestClassifier
from sklearn.grid_search import GridSearchCV

os.chdir('/Users/vijay/desktop/dmtermpaper/project/')
cmc = pd.read_csv('cmc.data', header=None)

#data = cmc.iloc[np.random.permutation(len(cmc))]
#X = data.ix[:,0:8]
#y = data.ix[:,9]

#MinMax scaling of the feature "Age" and "Children"
#cmc[0] = [float(i - cmc[0].min())/float(cmc[0].max()-cmc[0].min())for i in cmc[0]]
#cmc[3] = [float(i - cmc[3].min())/float(cmc[3].max()-cmc[3].min())for i in cmc[3]]

train, test = cross_validation.train_test_split(cmc, test_size=0.3, random_state=0)                                                       

train_X = train.ix[:,0:8]
train_Y = train.ix[:,9]

test_X = test.ix[:,0:8]
test_Y = test.ix[:,9]


n = range(1, 51)
paramGrid = dict(n_estimators=n)

random_forests = RandomForestClassifier()
grid = GridSearchCV(random_forests, paramGrid, cv=10, scoring='accuracy', n_jobs=-1)

start_time = time.time()
grid.fit(train_X, train_Y)
print("\n--- %s seconds ---\n" % (time.time() - start_time))

grid_mean_scores = [result.mean_validation_score for result in grid.grid_scores_]

plt.plot(n, grid_mean_scores)
plt.xlabel('Number of Trees')
plt.ylabel('Mean Cross-Validated Accuracy')

#examining  the best model
print grid.best_score_
print grid.best_params_
print grid.best_estimator_

#refitting the model with the best parameter & all of the training data and running the model against the test data.
start_time = time.time()
grid_test = grid.predict(test_X)
print("\n--- %s seconds ---\n" % (time.time() - start_time))

print metrics.accuracy_score(test_Y, grid_test)
print metrics.classification_report(test_Y, grid_test)
print metrics.confusion_matrix(test_Y, grid_test)
