#!/bin/sh
#Converts all the inkscape format svgs in skin-inkscape to plain format svgs

for f in `ls ../skin-inkscape`; do
inkscape --vacuum-defs --export-plain-svg=$f ../skin-inkscape/$f
done 
