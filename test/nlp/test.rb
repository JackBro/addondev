# -*- coding: utf-8 -*-

require_relative "mecab"

m = MecabLib::Mecab.new("")
#puts m.version
#puts m.sparse_tostr("本日は晴天なり。")
#node = m.sparse_tonode("本日は晴天なり。")
#while node.hasNext
# node = node.next
# print node.surface + " : " + node.pos + " : " + node.root + " : " + node.reading + " : " + node.pronunciation + "\n"
#end

f = open(File.expand_path(File.dirname(__FILE__)) + '/text.txt')
s = f.read
f.close

 h={}

node = m.sparse_tonode(s)
while node.hasNext
  node = node.next
  #next unless node.category == '名詞' # 名詞のみ解析対象とする
  #p node.pos.split(/,/)[0].force_encoding("UTF-8")
  #p '名詞'.force_encoding("UTF-8")

  next unless node.pos.split(/,/)[0].force_encoding("UTF-8") == '名詞'.force_encoding("UTF-8")
  if h[node.surface]
    h[node.surface] += 1 # 出現回数をカウント
  else
    h[node.surface] = 1
  end
end

    # 結果出力
    h.to_a.sort{ |a,b|
      (b[1] <=> a[1]) * 2 + (a[0] <=> b[0])
    }.each{ |e| puts "#{e[0]} #{e[1]}"}


