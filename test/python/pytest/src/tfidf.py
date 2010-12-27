#/usr/bin/python
#-*- encoding:utf-8 -*-

import os
import MeCab
from sys import argv
from math import log
from glob import glob

import json
from collections import defaultdict

def pp(obj):
    if isinstance(obj, list) or isinstance(obj, dict):
        orig = json.dumps(obj, indent=4)
        print eval("u'''%s'''" % orig).encode('utf-8')
    else:
        print obj

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
        
    def calcidf(self, data):
        df_sum=0
        df_num=0
        unkowndata =set()
        #data = self.calctf(doc)
        for k in data.keys():
            if data[k]['unkown']:
                df = self.js[k]
                tf=data[k]['tf']
                
                df_sum+=df
                df_num=+1
                
                data[k]['tfidf']=defaultdict(float)
                data[k]['tfidf']=tf * log( float(self.N) /float(df) );
            else:
                unkowndata.add(k)
                
        for k in unkowndata:
            tf=data[k]['tf']
            df = df_sum / df_num
            data[k]['tfidf']=defaultdict(float)   
            data[k]['tfidf']=tf * log( float(self.N) /float(df) );
            
        
    def calctf(self, doc):
        #self.unkown=set()    
        data ={}
        node = mecab.parseToNode(doc)
        while node:
            print "%s %s" % (node.surface, node.feature)
            if node.feature.split(",")[0] == "名詞":
                if node.surface not in data:
                    data[node.surface]={}
                    data[node.surface]['tf']=defaultdict(int)
                    data[node.surface]['tf']=0
                
                data[node.surface]['tf']+=1
                
                if self.js not in node.surface:
                    data[node.surface]['unkown']=defaultdict(bool)
                    data[node.surface]['unkown']=True
                    
                    
            node = node.next
            data.keys()
        return data
            
        

if __name__ == '__main__':
    tfidf = TFIDF();
    curdir = os.getcwd() # カレントディレクトリ名を取得
    path = os.path.join(curdir, "data\\test.json")
    #js = tfidf.load(path)
    #js = tfidf.js
    #s1 = js.encode('utf_8')
    #print json.dumps(s1)
    #print json.dumps(js)
    #print u'大砲' in js
    #print js[u'大砲']

    pp( tfidf.calctf("PythonからMeCabの形態素解析機能を使ってみました。"))
    
    #print tfidf.df(js)
    #print json.dumps(tfidf.df(js))
    
    
    