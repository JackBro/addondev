#!/usr/bin/perl

use strict;
use warnings;
use Storable qw(nstore);

my %color = (
	'あああ' => 'FF0000',
	'blue' => '0000FF',
	'white' => 'FFFFFF',
);
nstore \%color, 'my_color.dat';
