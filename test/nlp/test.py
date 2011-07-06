# -*- coding: utf-8 -*-

'''
Created on 2011/07/06

'''

#from numpy import *
#from scipy import linalg
#import numpy
#
#A = matrix([ [5, 8, 9, -4, 2, 4],
#             [2, -4, 9, 4, 3, 3],
#             [-3, 4, 8, 0, 5, 6],
#             [-2, 5, 4, 7, 0, 2] ])
#
#u, sigma, v = linalg.svd(A) # 特異値分解
#rank = shape(A)[0] # 階数
#u = matrix(u)
#s = matrix(linalg.diagsvd(sigma, rank, rank))
#v = matrix(v[:rank, :])
#print u*s*v
from numpy import *
import numpy as np
a = np.array([1,2,3,4,5])
b = np.array([[1,1,0,1,0],
              [0,1,1,0,1],
              [0,1,0,0,0],
              [1,0,1,0,0]
              ])

dele = []
i=0
c = np.sum(b,0)
for n in c:
  if n <= 1 :
    dele.append(i)
  i=i+1
    #print n 
#print kyou
#dele.reverse()
#print dele

d = delete(b,dele, axis=1)
print d

  
if __name__ == '__main__':
    pass