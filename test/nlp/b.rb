require 'rubygems'
require 'bayon'

docs = Bayon::Documents.new
docs.cluster_size_limit = 3
docs.add_document('Jacob'   , 'J-POP' => 10, 'J-R&B' => 6, 'Rock' => 4)
docs.add_document('Emma'    , 'Jazz' =>  8, 'Reggae'=> 9)
docs.add_document('Michael' , 'Classical music' => 4, 'World music' => 4)
docs.add_document('Isabella', 'Jazz' => 9, 'Metal' => 2, 'Reggae' => 6)
docs.add_document('Ethan'   , 'J-POP' => 4, 'Rock' => 3, 'Hip hop' => 3)
docs.add_document('Emily'   , 'Classical music' =>  8, 'Rock' => 1)

result = docs.do_clustering
#=> [["Emma", "Isabella"], ["Jacob", "Ethan"], ["Michael", "Emily"]]

result.each do |labels|
  puts labels.join(', ')
end

docs.output_similairty_point = true
result = docs.do_clustering

result.each do |label_points|
  puts label_points.map {|label, point|
    "#{label}(#{point})"
  }.join(', ')
end