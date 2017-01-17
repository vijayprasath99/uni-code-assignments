# -*- coding: utf-8 -*-
"""
Created on Wed Nov 17 13:27:55 2016

@author: vijay
"""

import os
import time
import pandas as pd
from sklearn import svm
from sklearn import metrics
from sklearn import cross_validation
from sklearn.grid_search import GridSearchCV


os.chdir('/Users/vijay/desktop/dmtermpaper/project/')
cmc = pd.read_csv('cmc.data', header=None)

train, test = cross_validation.train_test_split(cmc, test_size=0.3, random_state=0)
train_X = train.ix[:,0:8]
train_Y = train.ix[:,9]
test_X = test.ix[:,0:8]
test_Y = test.ix[:,9]

kernel = ['linear', 'rbf', 'sigmoid']
param_grid = dict(kernel=kernel)

svc = svm.SVC()

grid = GridSearchCV(svc, param_grid, cv=10, scoring='accuracy', n_jobs=-1)

start_time = time.time()
grid.fit(train_X, train_Y)
print("\n--- %s seconds ---\n" % (time.time() - start_time))

grid.grid_scores_

"""examining  the best model"""
print grid.best_score_
print"\n"
print grid.best_params_
print"\n"
print grid.best_estimator_

"""
refitting the model with the best parameter 
& all of the training data and running the model against the test data.
"""
start_time = time.time()
pred_Y = grid.predict(test_X)
print("\n--- %s seconds ---\n" % (time.time() - start_time))

print metrics.accuracy_score(test_Y, pred_Y)
print metrics.classification_report(test_Y, pred_Y)
print metrics.confusion_matrix(test_Y, pred_Y)


print metrics.classification_report(test_Y, pred_Y, target_names=['Class 0', 'Class 1', 'Class 2'])
