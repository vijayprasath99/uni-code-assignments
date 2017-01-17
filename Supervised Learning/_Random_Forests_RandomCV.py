# -*- coding: utf-8 -*-
"""
Created on Mon Nov 15 20:19:35 2016

@author: vijay
"""
import os
import time
import pandas as pd
from sklearn import metrics
from sklearn import cross_validation
from sklearn.ensemble import RandomForestClassifier
from sklearn.grid_search import RandomizedSearchCV

os.chdir('/Users/vijay/desktop/dmtermpaper/project/')
cmc = pd.read_csv('cmc.data', header=None)


train, test = cross_validation.train_test_split(cmc, test_size=0.3, random_state=0)                                                       

train_X = train.ix[:,0:8]
train_Y = train.ix[:,9]

test_X = test.ix[:,0:8]
test_Y = test.ix[:,9]

n_estimators = range(1, 51)
max_depth = range(1, 11)
paramGrid = dict(n_estimators=n_estimators, max_depth=max_depth)

random_forests = RandomForestClassifier()
rand = RandomizedSearchCV(random_forests, paramGrid, cv=10, scoring='accuracy', 
                          n_iter=50, random_state=5, n_jobs=-1)


start_time = time.time()
rand.fit(train_X, train_Y)
print("\n--- %s seconds ---\n" % (time.time() - start_time))


"""examining  the best model"""
print rand.best_score_
print rand.best_params_
print rand.best_estimator_

"""refitting the model with the best parameter 
& all of the training data and running the model against the test data.
"""
start_time = time.time()
grid_test = rand.predict(test_X)
print("\n--- %s seconds ---\n" % (time.time() - start_time))

print metrics.accuracy_score(test_Y, grid_test)
print "\n"
print metrics.classification_report(test_Y, grid_test)
print "\n"
print metrics.confusion_matrix(test_Y, grid_test)

