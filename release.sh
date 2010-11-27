#!/bin/bash
rm -rf bin/BoardCalc
svn export . bin/BoardCalc
cd bin
version=`grep 'MIDlet-Version:' MANIFEST.MF |cut -d: -f2|tr -d ' '`
tar c BoardCalc/ | gzip > BoardCalc-$version-src.tar.gz
rm -r BoardCalc
scp BoardCalc.ja* BoardCalc-$version-src.tar.gz rzymek@frs.sourceforge.net:uploads \
	&& rm BoardCalc-$version-src.tar.gz
svn cp https://earl.svn.sourceforge.net/svnroot/earl/other/BoardCalc \
	https://earl.svn.sourceforge.net/svnroot/earl/tags/BoardCalc-$version -m "tag BoardCalc-$version"
scp BoardCalc.ja* rzymek,earl@web.sf.net:htdocs/m/
