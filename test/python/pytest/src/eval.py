#coding:utf-8
import codecs
from naivebayes import NaiveBayes

# eval.py
# ナイーブベイズの性能評価

def evaluate(trainfile, testfile):
    # 訓練データをロード
    trainData = []
    fp = codecs.open(trainfile, "r", "utf-8")
    for line in fp:
        line = line.rstrip()
        temp = line.split()
        trainData.append(temp)
    fp.close()
    
    # ナイーブベイズを訓練
    nb = NaiveBayes()
    nb.train(trainData)
    print nb
    
    # テストデータを評価
    hit = 0
    numTest = 0
    fp = codecs.open(testfile, "r", "utf-8")
    for line in fp:
        line = line.rstrip()
        temp = line.split()
        correct = temp[0]    # 正解カテゴリ
        words = temp[1:]     # 文書：単語の集合
        predict = nb.classify(words)  # ナイーブベイズでカテゴリを予測
        if correct == predict:
            hit += 1  # 予測と正解が一致したらヒット！
        numTest += 1
    print "accuracy:", float(hit) / float(numTest)
    fp.close()

if __name__ == "__main__":
    evaluate("data/news20", "data/news20.t")
