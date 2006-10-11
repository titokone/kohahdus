TEXSEARCHPATH = :.:../pohjat/
DVIPS = dvips
RM = rm
MPAGE_CMD = mpage
DIA = dia

TEX_SRC = *.tex
DIA_SRC = 
BIB_SRC = ../pohjat/lahteet.bib

DOCNAME = dokumentti

$(DOCNAME).ps: $(DOCNAME).dvi docs.tex
	$(DVIPS) -t a4 $(DOCNAME).dvi


$(DOCNAME).dvi: $(TEX_SRC) $(patsubst %.dia,%.dia.eps,$(DIA_SRC)) $(BIB_SRC)
	@../pohjat/compile_doc.sh "$(TEXSEARCHPATH)" $(DOCNAME)

clean:
	@for f in $(TEX_SRC) ; do \
		f=`basename $$f .tex` ; \
		$(RM) -fv $$f.dvi $$f.log $$f.texlog $$f.lis $$f.list \
			$$f.aux $$f.toc $$f.lof $$f.lot \
			$$f.bbl $$f.blg $$f.bst $$f.idx $$f.tex~; \
	done
	@for f in $(DIA_SRC) ; do \
		$(RM) -fv $$f.eps ; \
	done
	@rm -fv $(DOCNAME).ps $(DOCNAME)-2.ps $(DOCNAME).pdf

ps: $(DOCNAME).ps
	gv $(DOCNAME).ps

pdf: $(DOCNAME).ps
	ps2pdf $(DOCNAME).ps $(DOCNAME).pdf

xdvi: $(DOCNAME).dvi
	xdvi $(DOCNAME).dvi

$(DOCNAME)-2.ps: $(DOCNAME).dvi
	$(DVIPS) -f $(DOCNAME).dvi | $(MPAGE_CMD) -2ot > $(DOCNAME)-2.ps ; \
	if [ ! -s $(DOCNAME)-2.ps ]; then \
		rm -f $(DOCNAME)-2.ps ; \
	fi

backup: clean
	tar fcz ~/ohtu-backup.tar.gz `find -maxdepth 1 -type f`
	
docs.tex: ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/*.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/User.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/Task.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/Course.java
	javadoc -docletpath ./TeXDoclet.jar -doclet sm.texdoclet.TeXDoclet ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/*.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/User.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/Task.java ../../EAssari/WEB-INF/src/fi/helsinki/cs/kohahdus/trainer/Course.java -include 

all: $(DOCNAME).ps $(DOCNAME)-2.ps

%.dia.eps: %.dia
	$(DIA) -ne $<.eps $<

.PHONY: clean ps xdvi $(DOCNAME)-2.ps backup all
