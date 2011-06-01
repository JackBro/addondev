# -*- coding: utf-8 -*-

#!/usr/bin/ruby
require 'Kconv'
require 'MeCab'
sentence = "太郎はこの本を二郎を見た女性に渡した。"

begin
			FEATURE_NAMES = { '品詞'=>0, '品詞細分類1'=>1, '品詞細分類2'=>2, '品詞細分類3'=>3, 
'活用形'=>4, '活用型'=>5, '原形'=>6, '読み'=>7, '発音'=>8 }     

     print MeCab::VERSION, "\n"	
     c = MeCab::Tagger.new(ARGV.join(" "))

     puts c.parse(sentence)

     n = c.parseToNode(sentence)

     while n do
       print n.surface.tosjis,  "\t", n.feature.tosjis, "\t", n.cost, "\n"
			 s = n.feature.split(/,/)
       print s[FEATURE_NAMES['原形']].tosjis, "\n"
       n = n.next
     end
     print "EOS\n";
     
     n = c.parseToNode(sentence)
     len = n.sentence_length;
     for i in 0..len
     	 b = n.begin_node_list(i)
       while b do
         printf "B[%d] %s\t%s\n", i, b.surface.tosjis, b.feature.tosjis;
	       b = b.bnext 
	     end
     	 e = n.end_node_list(i)	 
       while e do
         printf "E[%d] %s\t%s\n", i, e.surface.tosjis, e.feature.tosjis;
	       e = e.bnext 
	     end  
     end
     print "EOS\n";

     d = c.dictionary_info()
     while d do
        printf "filename: %s\n".tosjis, d.filename
        printf "charset: %s\n".tosjis, d.charset
        printf "size: %d\n".tosjis, d.size
        printf "type: %d\n".tosjis, d.type
        printf "lsize: %d\n".tosjis, d.lsize
        printf "rsize: %d\n".tosjis, d.rsize
        printf "version: %d\n".tosjis, d.version
        d = d.next
     end
     
rescue
      print "RuntimeError: ", $!, "\n";
end
