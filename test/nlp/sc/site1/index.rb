# -*- coding: utf-8 -*-


#http://www.pasonacareer.jp/list/j/5116/

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
doc.xpath("//h2").each do |t|
	if t["class"] == "headline_wide" then
		h={}
		name = t.xpath(".//a").inner_text
		h["name"] = tocsv(name)
		n = t.next.next
		if n["class"] == "tbl_jobDetail" then
			n.xpath(".//tbody/tr").each do |tb|
				#print tb.xpath(".//th").inner_text.tosjis, "\n"
				#print tb.xpath(".//td").inner_text.tosjis, "\n"
				key = tb.xpath(".//th").inner_text
				val = tb.xpath(".//td").inner_text
				if key == "応募資格"
					a,b = sp(val)
					h["必須"] = tocsv(a)
					h["歓迎"] = tocsv(b)
				else
					h[key] = tocsv(val)
				end
			end
			#break
		end
		#ans.push({"name"=>name, "pos"=>pos, "det"=>det})
		ans.push(h)
	end
end
print ans.size, "\n"
cs = k(ans)
File.open("abc.txt","w") do |io|
  io.write cs
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
