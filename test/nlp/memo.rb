# -*- coding: utf-8 -*-

require 'Kconv'

data =[]
data << File.read("作業.txt", :encoding => Encoding::UTF_8)

cnt=0
data.each do |s|
  d = s.split(/^\n/)
  d.each do |t|
		#data =[]
		filename =""
		title=""
    time=""
		body=""
    isfact=false
		sp = t.split(/\n/)
    sp.each do |line|
			if line =~ /^(\d\d\d\d\/\d\d\/\d\d\s\d\d:\d\d\s)(.*)/ then
        time = $1
				title = $2
				filename = title.gsub(/[\s\/:\*\?"<>\|]/,"_") + ".txt"
				#data.push($1)
				#data.push($2)
				#print ("time : " + $1).tosjis
				#print (" title : " + $2).tosjis + "\n"
	    elsif line =~ /^(\t)(.*)/ then
	    	if(line.include?("原因") || line.include?("現象") || line.include?("解消"))
					
          isfact = true
	        #print line.tosjis+"\n"
	    	end
				body += $2+"\n"
			end
    end
		#data.push(body)
		#if isfact then
    #  print ("time : #{time}").tosjis
    #  print (" title : #{title}").tosjis + "\n"
	  #  print ("body : #{body}").tosjis
		#  print "---\n".tosjis
    #end
		f.wr(cnt, 0, time, title, body)
		cnt=cnt+1
  end
end