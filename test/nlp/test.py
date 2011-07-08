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

def kyoki(data, minfreq=2):
  dele = []
  i=0
  c = np.sum(data,0)
  for n in c:
    if n < minfreq :
      dele.append(i)
    i=i+1
    
  d = delete(data,dele, axis=1)
  return d

def dimReducShare(u, v, d, share=0.5 ):
  sigma = d
  ss = sigma.sum()
  ind = max(where(sigma.cumsum()/ss <= 0.5))
  dim = max(ind)
  if dim < 2:
    dim=2
    
  return  u[:, :dim], v[:, :dim], sigma[:dim]

def myQuery(qtext, term):
  i=0
  deli = []
  for n1 in w1:
    f=False
    for n2 in q:
      if n2==n1:
        f=True
        #deli.append(i)
        deli.append(1)
        break
    if f==False:
      deli.append(0)
    i=i+1
  #print asarray(deli)  
  return asarray(deli)

#def myCosine(a, b):
#  numpy.cross()

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

print d.cumsum(axis=1)

u3 = d[:, :2]
print u3

u, sigma, v = linalg.svd(b) # 特異値分解
print sigma
ss = sigma.sum()
print sigma.cumsum()/ss

ind = max(where(sigma.cumsum()/ss <= 0.5))
dim = max(ind)
if dim < 2:
  dim=2
print u[:, :dim]
print v[:, :dim]
print sigma[:dim]
  
w1 = np.array(["a","b","c","d","e", "f"])
q = np.array(["a","d","e"])
w2 = w1.copy()
i=0
deli = []
for n1 in w1:
  f=False
  for n2 in q:
    if n2==n1:
      f=True
      #deli.append(i)
      deli.append(1)
      break
  if f==False:
    deli.append(0)
  i=i+1
  
print asarray(deli)

#def myfunc(m):
#  return (m[0]+m[-1])/2
#print apply_along_axis(myfunc,0,b)
print corrcoef(b.T)
#res=[]
#for x in b:
#  for y in b:
#    print corrcoef(x,y)
#print res
#print w2.astype(float64) 

if __name__ == '__main__':
    pass