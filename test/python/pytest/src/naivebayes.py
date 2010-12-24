#coding:utf-8
import MeCab

import math
import sys

from collections import defaultdict


#tagger = MeCab.Tagger("-Ochasen")
#node = tagger.parseToNode("PythonからMeCabの形態素解析機能を使ってみました。")
#while node:
#    print "%s %s" % (node.surface, node.posid)
#    node = node.next

class NaiveBayes:
    def __init__(self):
        self.cats = set()
        self.wordcount ={}
        self.catcount={}
        self.vocabularies = set()
        self.denominator = {}

    """"ナイーブベイズ分類器の訓練"""
    def train(self, data):
        for d in data:
            cat = d[0]
            self.cats.add(cat)
        for cat in self.cats:
            self.wordcount[cat] = defaultdict(int)
            self.catcount[cat]=0        
        for d in data:
            cat,doc=d[0],d[1:]
            self.catcount[cat]+=1
            for wc in doc:
                word, count = wc.split(":")
                count = int(count)
                self.vocabularies.add(word)
                self.wordcount[cat][word]+=count
        for cat in self.cats:
            self.denominator[cat] = sum(self.wordcount[cat].values())+ len(self.vocabularies)
    """単語の条件付き確率 P(word|cat) を求める"""
    def wordProb(self, word, cat):
        return float(self.wordcount[cat][word] + 1) / float(self.denominator[cat])
#        if self.wordcount[cat][word] != 0:
#            return float(self.wordcount[cat][word]) / float(self.denominator[cat])
#        else:
#            return float(self.wordcount[cat][word] + 1) / float(self.denominator[cat]+ len(self.vocabularies))
    def score(self,doc,cat):
        total = sum(self.catcount.values())
        score = math.log(float(self.catcount[cat])/total)
        for wc in doc:
            word, count = wc.split(":")
            count = int(count)
            for i in range(count):
                score+=math.log(self.wordProb(word, cat))
        return score
    def classify(self, doc):
        """事後確率の対数 log(P(cat|doc)) がもっとも大きなカテゴリを返す"""
        best = None
        max = -sys.maxint
        for cat in self.catcount.keys():
            p = self.score(doc, cat)
            if p > max:
                max = p
                best = cat
        return best  
    def __str__(self):
        total = sum(self.catcount.values())  # 総文書数
        return "documents: %d, vocabularies: %d, categories: %d" % (total, len(self.vocabularies), len(self.cats))        
       
def sample1():
    data = [["yes", "Chinese:2", "Beijing:1"],
            ["yes", "Chinese:2", "Shanghai:1"],
            ["yes", "Chinese:1", "Macao:1"],
            ["no", "Tokyo:1", "Japan:1", "Chinese:1"]]
    
    nb = NaiveBayes();
    nb.train(data);
    #print nb.cats
    
    print "P(Chinese|yes) = ", nb.wordProb("Chinese", "yes")
    print "P(Tokyo|yes) = ", nb.wordProb("Tokyo", "yes")
    print "P(Japan|yes) = ", nb.wordProb("Japan", "yes")
    print "P(Chinese|no) = ", nb.wordProb("Chinese", "no")
    print "P(Tokyo|no) = ", nb.wordProb("Tokyo", "no")
    print "P(Japan|no) = ", nb.wordProb("Japan", "no")
    
    # テストデータのカテゴリを予測
    test = ["Chinese", "Chinese", "Chinese", "Tokyo", "Japan"]
    print "log P(yes|test) =", nb.score(test, "yes")
    print "log P(no|test) =", nb.score(test, "no")
    print nb.classify(test)

    