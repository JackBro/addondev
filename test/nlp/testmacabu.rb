# -*- coding: utf-8 -*-

require 'MecabFunc'
require 'tfidf_ja'
require 'bayon'


class Wakati
  @m=nil
  def initialize(p)
    @m=Mecab.new(p)
  end

  def words(text)
    res=[]
    node = @m.sparse_tonode(text)
    while node.hasNext
      node = node.next
      
      if(node.surface !="*" && (node.feature=='67' ||node.feature=='59' || node.feature=='38' || node.feature=='31')) then
        #puts "node.surface=#{node.surface}"
        res.push(node.root)
      end
    end
    return res
  end

  def des
    @m.destroy
  end
end


w= Wakati.new("-Otest")
ti = TfIdf::Ja.new

docs = Bayon::Documents.new
docs.cluster_size_limit = 30

files = Dir.glob('data/*.txt')
files.each do |item|
  if /data\/(\d+)\.txt/ =~ item
    index = $1
    puts item
    #s = IO.read item
    s=""
    IO.foreach(item) do |txt|
      if /To:\s/ =~ txt then
        #puts "TO :txt = #{txt}"
      else
        s=s+txt+"\n"       
      end
    end
    res = w.words(s)
    
    ti.reset
    tires = ti.tfidf(res)

    kindex=0
    sortres={}
    tires.sort{|a, b|
      b[1] <=> a[1]
    }.each{|name, value|
      if name != "*" then
        sortres[name]=value
        #puts "#{name}'s is #{value} cm"
      end
      if kindex>=10 then
        break
      end
      kindex+=1
    }

    #puts sortres
    docs.add_document(index, sortres)
  end
end

w.des()

result = docs.do_clustering
docs.output_similairty_point = true
result = docs.do_clustering

result.each do |label_points|
  puts label_points.map {|label, point|
    "#{label}(#{point})"
  }.join(', ')
end
