#coding:utf-8
import sys
import os
from collections import defaultdict

# trans_data.py
# news20のデータセットをナイーブベイズで扱えるデータ形式に変換する
# http://people.csail.mit.edu/jrennie/20Newsgroups/
# 出力データフォーマット
# 1列目にカテゴリ、2列目以降は単語と出現頻度の組を列挙
# [category] [word:count] [word:count] ...  <- doc1
# [category] [word:count] [word:count] ...  <- doc2

def trans_data(labelfile, datafile, outfile):
    # カテゴリをロード
    category = []
    fp = open(os.path.join(os.getcwd(), "data\\train.map"))  # test.mapでも同じ
    for line in fp:
        line = line.rstrip()
        category.append(line.split()[0])
    fp.close()
    # ストップワードをロード
    stopwords = []
    fp = open(os.path.join(os.getcwd(), "data\\stopwords.txt"))
    for line in fp:
        line = line.rstrip()
        stopwords.append(line)
    fp.close()
    
    # ボキャブラリをロード
    vocabulary = []
    fp = open(os.path.join(os.getcwd(), "data\\vocabulary.txt"))
    for line in fp:
        line = line.rstrip()
        vocabulary.append(line)
    fp.close()
    
    # ラベルをロード
    train_label = []
    fp = open(labelfile)
    for line in fp:
        line = line.rstrip()
        # 配布ファイルのカテゴリIDは1から始まるので1をひく
        idx = int(line) - 1
        cat = category[idx]
        train_label.append(cat)
    fp.close()
    
    # 総文書数
    num = len(train_label)
    
    # 変換
    train_data = []
    for i in range(num):
        train_data.append([])
    
    fp = open(datafile)
    for line in fp:
        line = line.strip()
        temp = line.split()
        docidx, wordidx, count = int(temp[0]), int(temp[1]), int(temp[2])
        # 配布データの文書IDと単語IDは1から始まるので1を引いたインデックスを使う
        word = vocabulary[wordidx-1]
        if not word in stopwords:  # ★ストップワードに登録されていない単語だけ追加
            train_data[docidx-1].append("%s:%d" % (word, count))
    fp.close()
    
    # ファイルに出力
    fp = open(outfile, "w")
    for i in range(num):
        fp.write("%s %s\n" % (train_label[i], " ".join(train_data[i])))
    fp.close()

if __name__ == "__main__":
    # 訓練データを変換
    trans_data(os.path.join(os.getcwd(), "data\\train.label"), os.path.join(os.getcwd(), "data\\train.data"), os.path.join(os.getcwd(), "data\\news20"))
    # テストデータを変換
    trans_data(os.path.join(os.getcwd(), "data\\test.label"), os.path.join(os.getcwd(), "data\\test.data"), os.path.join(os.getcwd(), "data\\news20.t"))
