#!/bin/bash
texsearch=$1
docname=$2
prev_toc_md5sum="X"
if [ -e $docname.toc ]; then
	toc_md5sum=`md5sum $docname.toc`
fi
maxruns=10
while [ "z$prev_toc_md5sum" != "z$toc_md5sum" -a $maxruns -gt 0 ]; do
	TEXINPUTS=$texsearch latex $docname.tex
	if [ "$?" != 0 ]; then
		exit 1
	fi
	prev_toc_md5sum=$toc_md5sum
	toc_md5sum=`md5sum $docname.toc`
	if fgrep 'Citation' $docname.log ; then
		BSTINPUTS=$texsearch BIBINPUTS=$texsearch bibtex $docname
		prev_toc_md5sum="X" 
	fi
	let maxruns--
done
if [ $maxruns -eq 0 ]; then
	echo "==="
	echo "Maximum number of runs exceeded, probably stuck in infinite loop"
	exit 1
fi
while fgrep -q 'Rerun to get' $docname.log ; do
	TEXINPUTS=$texsearch latex $docname.tex
	if [ "$?" != 0 ]; then
		exit 1
	fi
done
