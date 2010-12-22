#!/usr/bin/perl

use strict;
use warnings;
use Storable;
use Data::Dumper;
use JSON;

#print Dumper(retrieve ('df.st'));
my $bbb = retrieve ('D:\data\src\nlp\df.st');
#my $i=0;
#while (my($k,$v) = each %$bbb){
	# print qq($k,$v\n);
	# if($i==10){
		# last;
	# }
	# $i++;
# }
my $allList_json = JSON->new->encode($bbb);
my $fname = 'trinity777.txt';
open(FILE, ">$fname") or die;
#while (my($j) = each %$allList_json){
#	print FILE $j, "\n";
#}
print FILE $allList_json;
close(FILE);
