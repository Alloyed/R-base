#!/bin/sh
#Converts all the inkscape format svgs in skin-inkscape to plain format svgs

for f in `ls ../skin-inkscape`; do
	inkscape --export-plain-svg=$f ../skin-inkscape/$f
	nf=$(basename $f .svg)
	echo $nf
	scour -i$nf.svg -o$nf.svgz
	rm -f $nf.svg
done 
