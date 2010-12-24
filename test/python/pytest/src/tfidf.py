#/usr/bin/python
#-*- encoding:utf-8 -*-
import os
import MeCab
from sys import argv
from math import log
from glob import glob

import json


mecab = MeCab.Tagger("-Ochasen")

class TFIDF:
    
    def __init__(self):
        self.N = 20000000000
    #    self.json_path = json_path
        
    def load(self, json_path):
        f = open(json_path, 'r')
        self.js = json.load(f,  'utf-8') 
        #return f.readlines()

    def idf(self, word):
        word_cnt=self.js[word]
        return log(1.0 *self.N/word_cnt)
        

    def tf(self, doc, word):
        
        
        
        

if __name__ == '__main__':
    tfidf = TFIDF();
    js = tfidf.load("data/test.json")
    #s1 = js.encode('utf_8')
    #print json.dumps(s1)
    print json.dumps(js)
    print js[u'大砲']
    #print tfidf.df(js)
    #print json.dumps(tfidf.df(js))
    
    
    