# -*- coding: utf-8 -*-


#http://embeddedcontrol-job.com/private/index.html?utm_source=overture&utm_medium=cpc&utm_campaign=embeddedcontrol_search

require 'nokogiri'
require 'kconv'

def tocsv(lines)
	lines.gsub!(/,/, '、')
	s = ""
	lines.split("\n").each{|l|
		if l!="" && /$。/!~l
			l=l+"。" #.force_encoding("UTF-8")
		end
		s=s+l
	}
	return s
end

def sp(oubo)
	re ={}
	h = oubo.scan(/【(.*)】([^【】]+)/)
	h.each{|v|
		if v[0].index("必須")
			re["必須"] = v[1]
		elsif v[0].index("歓迎")
			re["歓迎"] = v[1]
		end
	}
	if re.size ==0
		re["必須"] = oubo
		re["歓迎"] = ""
	end

	if re["歓迎"] == nil
		re["歓迎"] = ""
	end

	#a=""
	#re["必須"].split("\n").each{|l|
	#	#print l, "\n"
	#	if /$。/!~l
	#		l=l+"。" #.force_encoding("UTF-8")
	#	end
	#	a=a+l
	#}
	#b=""
	#re["歓迎"].split("\n").each{|l|
	#	#print l, "\n"
	#	if /$。/!~l
	#		l=l+"。" #.force_encoding("UTF-8")
	#	end
	#	b=b+l
	#}
	#return a,b
	return re["必須"], re["歓迎"]
end

#id	名前	url	何	内容	必須	歓迎
def k(hs)
	re = ""
	hs.each{|h|
		re = re + ["",h["name"],"",h["ポジション"],h["仕事内容"],h["必須"],h["歓迎"]].join(',') + "\n"
	}
	return re
end
#name, pos, det, ess, well


ans=[]

doc = Nokogiri::HTML.parse(File.read("index.html", :encoding => Encoding::UTF_8), nil, 'UTF-8')
doc.xpath("//a[@class='moreDetail']").each do |a|
	print a["href"].to_s.tosjis, "\n"
end

#print ans[0].size, "\n"
#print ans[0]["ポジション"].tosjis, "\n"
#print ans[0]["仕事内容"].tosjis, "\n"
#print ans[0]["必須"].tosjis, "\n"
#doc.xpath("//table").each do |t|
#	if t["class"] == "tbl_jobDetail" then
#		t.xpath(".//tbody/tr").each do |tb|
#			print tb.xpath(".//th").to_s.tosjis, "\n"
#			print tb.xpath(".//td").to_s.tosjis, "\n"
#		end
#		break
#	end
#end
