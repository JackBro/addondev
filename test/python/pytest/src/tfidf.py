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
        
    def calcidf(self, doc):
        df_sum=0
        df_num=0
        data = self.self(doc)
        for k in data.keys():
            if self.js in k:
                jsdata = self.js[k]
                tf=data[k]['tf']
                df = d
                d['tfidf']=defaultdict(float)
                d['tfidf']=tf * log( float(self.N) /float(df) );
            else:
                
            
            
        
    def calctf(self, doc):
        #self.unkown=set()
        
        wordcount ={{}}
        node = mecab.parseToNode(doc)
        while node:
            print "%s %s" % (node.surface, node.feature)
            if node.feature.split(",")[0] == "名詞":
                if node.surface not in wordcount:
                    wordcount[node.surface]['tf']=defaultdict(int)
                    wordcount[node.surface]['tf']=0
                
                wordcount[node.surface]['tf']+=1
                
                #if self.js not in node.surface:
                #    self.unkown.add(node.surface)
                    
            node = node.next
            wordcount.keys()
        return wordcount
            
        

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
    
    
    