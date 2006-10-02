%%
%% This is file `ohtu.cls',
%% orginally generated with the docstrip utility, but later
%% modified manually
%%
%% The original source files were:
%%
%% tktltiki.dtx  (with options: `luokka')
%% 
%% This is a generated file.
%% 
%% Copyright (C) 2002 Mikael Puolakka.
%% Modifications by Turjo Tuohiniemi 2004.
%% 
%% This file may be distributed and/or modified under the
%% conditions of the LaTeX Project Public License, either
%% version 1.2 of this license or (at your option) any later
%% version. The latest version of this license is in
%% 
%%    http://www.latex-project.org/lppl.txt
%% 
%% and version 1.2 or later is part of all distributions of
%% LaTeX version 1999/12/01 or later.
%% 
%% \CharacterTable
%%  {Upper-case    \A\B\C\D\E\F\G\H\I\J\K\L\M\N\O\P\Q\R\S\T\U\V\W\X\Y\Z
%%   Lower-case    \a\b\c\d\e\f\g\h\i\j\k\l\m\n\o\p\q\r\s\t\u\v\w\x\y\z
%%   Digits        \0\1\2\3\4\5\6\7\8\9
%%   Exclamation   \!     Double quote  \"     Hash (number) \#
%%   Dollar        \$     Percent       \%     Ampersand     \&
%%   Acute accent  \'     Left paren    \(     Right paren   \)
%%   Asterisk      \*     Plus          \+     Comma         \,
%%   Minus         \-     Point         \.     Solidus       \/
%%   Colon         \:     Semicolon     \;     Less than     \<
%%   Equals        \=     Greater than  \>     Question mark \?
%%   Commercial at \@     Left bracket  \[     Backslash     \\
%%   Right bracket \]     Circumflex    \^     Underscore    \_
%%   Grave accent  \`     Left brace    \{     Vertical bar  \|
%%   Right brace   \}     Tilde         \~}
\NeedsTeXFormat{LaTeX2e}[2000/06/01]
\ProvidesClass{ohtu}
  [2004/01/19 v1.0 Dokumenttiluokka Ohjelmistotuotantoprojektia varten]
\newif\if@finnish
\newif\if@swedish
\newif\if@english
\newif\if@emptyfirstpagenumber

\DeclareOption{finnish}{\@finnishtrue}
\DeclareOption{swedish}{\@swedishtrue}
\DeclareOption{english}{\@englishtrue}
\DeclareOption{emptyfirstpagenumber}{\@emptyfirstpagenumbertrue}
\newif\if@defaulttypesize
\@defaulttypesizetrue
\newif\if@defaultpaper
\@defaultpapertrue
\newif\if@defaulttitlepage
\@defaulttitlepagetrue

\DeclareOption{10pt}{
  \@defaulttypesizefalse
  \PassOptionsToClass{10pt}{article}}
\DeclareOption{11pt}{
  \@defaulttypesizefalse
  \PassOptionsToClass{11pt}{article}}

\DeclareOption{a5paper}{
  \@defaultpaperfalse
  \PassOptionsToClass{a5paper}{article}}
\DeclareOption{b5paper}{
  \@defaultpaperfalse
  \PassOptionsToClass{b5paper}{article}}
\DeclareOption{letterpaper}{
  \@defaultpaperfalse
  \PassOptionsToClass{letterpaper}{article}}
\DeclareOption{legalpaper}{
  \@defaultpaperfalse
  \PassOptionsToClass{legalpaper}{article}}
\DeclareOption{executivepaper}{
  \@defaultpaperfalse
  \PassOptionsToClass{executivepaper}{article}}

\DeclareOption{notitlepage}{
  \@defaulttitlepagefalse
  \PassOptionsToClass{notitlepage}{article}}

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{article}}
\ProcessOptions\relax
\if@defaulttypesize
  \PassOptionsToClass{12pt}{article}
\fi
\if@defaultpaper
  \PassOptionsToClass{a4paper}{article}
\fi
\if@defaulttitlepage
  \PassOptionsToClass{titlepage}{article}
\fi
\ProcessOptions\relax
\LoadClass{article}[2000/05/19]
\RequirePackage{latexsym}[1998/08/17 v2.2e Standard LaTeX package (lasy symbols)]
\RequirePackage[latin9]{inputenc}[2000/07/01 v0.996 Input encoding file ]
\RequirePackage[T1]{fontenc}[2000/08/30 v1.91 Standard LaTeX package]
\if@finnish
  \RequirePackage[finnish]{babel}[2001/03/01 v3.7h The Babel package]
\else
  \if@english
    \RequirePackage[english]{babel}[2001/03/01 v3.7h The Babel package]
  \else
    \if@swedish
      \RequirePackage[swedish]{babel}[2001/03/01 v3.7h The Babel package]
    \else
      \RequirePackage[finnish]{babel}[2001/03/01 v3.7h The Babel package]
    \fi
  \fi
\fi
\RequirePackage{geometry}[1999/10/07 v2.2 Page Geometry]
\newcommand{\defaultsettings}{%
  \if@twoside
    \geometry{top=2.5cm, left=2.8cm, right=3.2cm,
              textwidth=15cm, textheight=23cm,
              headheight=0.5cm, headsep=0.5cm}%
  \else
    \geometry{top=2.5cm, left=3.5cm, right=2.5cm,
              textwidth=15cm, textheight=23cm,
              headheight=0.5cm, headsep=0.5cm}%
  \fi
}
\defaultsettings
\setlength{\parindent}{0pt}
\setlength{\parskip}{1ex}
\pagestyle{myheadings}
\markright{}
\pagenumbering{roman}
\newcommand{\onehalfspacing}{%
  \ifcase\@ptsize\relax % 10pt
    \renewcommand{\baselinestretch}{1.25}%
  \or % 11pt
    \renewcommand{\baselinestretch}{1.213}%
  \or % 12pt
    \renewcommand{\baselinestretch}{1.241}%
  \fi
  \normalsize
}
\newcommand{\doublespacing}{
  \ifcase\@ptsize\relax % 10pt
    \renewcommand{\baselinestretch}{1.667}
  \or % 11pt
    \renewcommand{\baselinestretch}{1.618}
  \or % 12pt
    \renewcommand{\baselinestretch}{1.655}
  \fi
  \normalsize
}
\newcommand{\singlespacing}{%
  \renewcommand{\baselinestretch}{1.0}%
  \normalsize
}
\newcommand{\mytableofcontents}{%
  \tableofcontents
  \newpage
  \pagenumbering{arabic}
  \if@emptyfirstpagenumber
    \thispagestyle{empty}
  \fi
}
\newcounter{appendix}
\newcommand{\appendices}{%
  \setcounter{appendix}{1}%
  \newpage
  %\pagestyle{empty}
  \pagestyle{myheadings}
  \markright{}
  \appendix
  \addtocontents{toc}{\protect \contentsline {section}{\enclname}{}}
  \setcounter{page}{0}
}
\newcommand{\internalappendix}[2]{%
  \addtocounter{appendixpage}{\value{page}}%
  \newpage
  \setcounter{page}{1}%
  \section*{\appendixname\ #1. #2}
  \addtocontents{toc}{
    \protect \contentsline {section}{\hspace{0.5cm}#1 #2}{}}
  \addtocounter{appendix}{+1}%
}
\newcommand{\externalappendix}[2]{%
  \addtocontents{toc}{
    \protect \contentsline {section}{\hspace{0.5cm}#1 #2}{}}
  \addtocounter{appendix}{+1}%
}
\newcounter{appendixpage}
\newcommand{\numberofappendixpages}[1][0]{%
  \addtocounter{appendixpage}{#1}%
  \ref{@lastappendixpage}%
}

\AtEndDocument{%
  \addtocounter{appendixpage}{\value{page}}%
  \immediate\write\@auxout{\string
    \newlabel{@lastappendixpage}{{\theappendixpage}{\thepage}}}%
}
\newcommand{\lastpage}{%
  \label{lastpage}%
}

\newcommand{\numberofpages}{%
  \@ifundefined{r@lastpage}{0}{\pageref{lastpage}}%
}
\renewcommand{\and}{%
  \\ % Rivinvaihto.
  \hspace{1em}%
}
\RequirePackage{openbib}
\addto\captionsfinnish{\def\refname{Lähteet}}
\addto\captionsenglish{\def\enclname{Appendices}}
\addto\captionsswedish{\def\enclname{Bilagor}}
\renewenvironment{thebibliography}[1]
     {\section*{\refname
        \@mkboth{\uppercase{\refname}}{\uppercase{\refname}}
        \addcontentsline{toc}{section}{\refname}}%
      \list{\@biblabel{\arabic{enumiv}}}%
           {\settowidth\labelwidth{\makebox[5em]{}}%
            \leftmargin\labelwidth
            \advance\leftmargin\labelsep
            \usecounter{enumiv}%
            \let\p@enumiv\@empty
            \renewcommand\theenumiv{\arabic{enumiv}}}%
        \renewcommand\newblock{\hskip .11em \@plus.33em \@minus.07em}%
      \sloppy\clubpenalty4000\widowpenalty4000%
      \sfcode`\.=\@m}
     {\def\@noitemerr
       {\@latex@warning{Empty `thebibliography' environment}}%
      \endlist}
\def\@biblabel#1{#1}
\def\datefinnish{%
  \def\today{\number\day.\number\month.\number\year}}
\newcommand{\level}[1]{\gdef\@level{#1}}
\level{}
\newcommand{\defprojectgroup}[1]{\gdef\@projectgroup{#1}}
\newcommand{\defgroupmembers}[1]{\gdef\@groupmembers{#1}}
\newcommand{\membername}[1]{\hspace*{7mm}#1}
\newcommand{\defprojectclient}[1]{\gdef\@projectclient{#1}}
\newcommand{\defprojectmasters}[1]{\gdef\@projectmasters{#1}}
\newcommand{\mastername}[1]{\hspace*{7mm}#1}
\newcommand{\defprojecthomepage}[1]{\gdef\@projecthomepage{#1}}

%\newenvironment{groupmembers}[0]{\gdef\projectgroupmembers{}{}

\addto\captionsfinnish{%
  \def\dateofacceptance{hyväksymispäivä}%
  \def\grade{arvosana}%
  \def\instructor{arvostelija}%
  \def\uh{HELSINGIN YLIOPISTO}%
  \def\dcs{Tietojenkäsittelytieteen laitos}%
  \def\helsinki{Helsinki}%
  \def\ccs{ACM Computing Classification System (CCS):}%
  \def\seproject{Ohjelmistotuotantoprojekti}%
  \def\course{Kurssi}%
  \def\credits{ov}%
  \def\projectgrouptitle{Projektiryhmä}%
  \def\projectinstructor{Ryhmän ohjaaja}%
  \def\projectclienttitle{Asiakas}%
  \def\projectmastertitle{Johtoryhmä}%
  \def\projecthomepagetitle{Kotisivu}%
  \def\versionhistory{Versiohistoria}%
  \def\versiontitle{Versio}%
  \def\datetitle{Päiväys}%
  \def\changestitle{Tehdyt muutokset}%
}
\addto\captionsenglish{%
  \def\dateofacceptance{Date of acceptance}%
  \def\grade{Grade}%
  \def\instructor{Instructor}%
  \def\uh{UNIVERSITY OF HELSINKI}%
  \def\dcs{Department of Computer Science}%
  \def\helsinki{Helsinki}%
  \def\ccs{ACM Computing Classification System (CCS):}%
  \def\seproject{Software Engineering Project}%
  \def\course{Course}%
  \def\credits{cr}%
  \def\projectgrouptitle{Project Group}%
  \def\projectinstructor{Instructor}%
  \def\projectclienttitle{Client}%
  \def\projectmastertitle{Project Masters}%
  \def\projecthomepagetitle{Homepage}%
  \def\versionhistory{Change Log}%
  \def\versiontitle{Version}%
  \def\datetitle{Date}%
  \def\changestitle{Modifications}%
}
\addto\captionsswedish{%
  \def\dateofacceptance{godk.datum}%
  \def\grade{vitsord}%
  \def\instructor{bedömare}%
  \def\uh{HELSINGFORS UNIVERSITET}%
  \def\dcs{Institutionen för datavetenskap}%
  \def\helsinki{Helsingfors}%
  \def\ccs{ACM Computing Classification System (CCS):}%
  \def\seproject{Programvaruproduktion}%
  % Nämä voisi kääntää joskus.
  \def\course{???}%
  \def\credits{??}%
  \def\projectgrouptitle{???}%
  \def\projectinstructor{???}%
  \def\projectclienttitle{???}%
  \def\projectmastertitle{???}%
  \def\projecthomepagetitle{???}%
  \def\versionhistory{???}%
  \def\versiontitle{???}%
  \def\datetitle{???}%
  \def\changestitle{???}%
}

\renewcommand{\maketitle}{\begin{titlepage}%
  \vspace*{10mm}
  \vspace*{1em}
  \vspace*{70mm}

  {\large\bf \@title}

  \vspace{5mm}

  \@projectgroup

  \vspace{75mm}

  \helsinki\ \@date

  \seproject

  \uh\\
  \dcs

  \end{titlepage}%
}
\newcommand{\projectinfopage}[0]{
  \thispagestyle{empty}
  {\bf \course}
  \\\hspace*{7mm}581260 \seproject\ (\(6\) \credits)

  \vspace{2mm}
  {\bf \projectgrouptitle}
  \\\@groupmembers

  \vspace{2mm}
  {\bf \projectclienttitle}
  \\\hspace*{7mm}\@projectclient

  \vspace{2mm}
  {\bf \projectmastertitle}
  \\\@projectmasters

  \vspace{2mm}
  {\bf \projecthomepagetitle}
  \\\hspace*{7mm}{\tt \@projecthomepage}

  \vspace{2mm}
  {\bf \versionhistory}
  \\\hspace*{7mm}\begin{tabular}{lll}
  \versiontitle & \datetitle & \changestitle \\
  \hline\input{versio}
  \end{tabular}

  \newpage
}

\def\division{\char'057}

\newcommand{\faculty}[1]{\gdef\@faculty{#1}}
\newcommand{\department}[1]{\gdef\@department{#1}}
\newcommand{\subject}[1]{\gdef\@subject{#1}}
\newcommand{\depositeplace}[1]{\gdef\@depositeplace{#1}}
\newcommand{\additionalinformation}[1]{\gdef\@additionalinformation{#1}}
\newcommand{\numberofpagesinformation}[1]{\gdef\@numberofpagesinformation{#1}}
\newcommand{\classification}[1]{\gdef\@classification{#1}}
\newcommand{\keywords}[1]{\gdef\@keywords{#1}}

\faculty{}
\department{}
\subject{}
\depositeplace{}
\additionalinformation{}
\numberofpagesinformation{}
\classification{}
\keywords{}
\newsavebox{\@abstract}
\newenvironment{@summary}{
  \begin{lrbox}{\@abstract}
    \begin{minipage}[t]{5.95in}
      \setlength{\parskip}{2ex}
}{

      \if \@classification
      \else
        \ccs\ \@classification
      \fi
    \end{minipage}
  \end{lrbox}
  \put(58, 650){\mbox{\usebox{\@abstract}}}
}

\renewenvironment{abstract}{%
\if@twoside
\begin{picture}(580,845)(71,-94)%
\else
\begin{picture}(580,845)(74,-94)%
\fi
\put(58,  784){\makebox(100, 8)[l]{\@faculty}}
\put(289, 784){\makebox(100, 8)[l]{\@department}}
\put(58,  761){\makebox(100, 8)[l]{\@projectgroup}}
\put(58,  727){\makebox(200, 8)[l]{\@title}}
\put(58,  704){\makebox(100, 8)[l]{\@subject}}
\put(58,  681){\makebox(100, 8)[l]{\@level}}
\put(212, 681){\makebox(100, 8)[l]{\@date}}
\put(366, 681){\makebox(100, 8)[l]{\@numberofpagesinformation}}
\put(58,  94) {\makebox(100, 8)[l]{\@keywords}}
\put(58,  72) {\makebox(100, 8)[l]{\@depositeplace}}
\put(58,  35) {\makebox(100, 8)[l]{\@additionalinformation}}
\begin{@summary}}
{\end{@summary}
\put(53,30){\framebox(462,786){}}
\put(53,779){\line(1,0){462}}
\put(53,756.24){\line(1,0){462}}
\put(53,722.1){\line(1,0){462}}
\put(53,699.34){\line(1,0){462}}
\put(53,676.58){\line(1,0){462}}
\put(53,67){\line(1,0){462}}
\put(53,89.76){\line(1,0){462}}
\put(53,112.52){\line(1,0){462}}
\put(284,779){\line(0,1){37}}
\put(207,676.58){\line(0,1){22.76}}
\put(361,676.58){\line(0,1){22.76}}
\put(58,809){\makebox(150,6)[l]{
\tiny Tiedekunta\division Osasto --- Fakultet\division Sektion --- Faculty}}
\put(289,809){\makebox(100,6)[l]{\tiny Laitos --- Institution --- Department}}
\put(58,773){\makebox(100,5)[l]{\tiny Tekij\"a --- F\"orfattare --- Author}}
\put(58,750){\makebox(100,5)[l]{\tiny Ty\"on nimi --- Arbetets titel --- Title}}
\put(58,716){\makebox(100,5)[l]{\tiny Oppiaine --- L\"aro\"amne --- Subject}}
\put(58,693){\makebox(100,5)[l]{\tiny Ty\"on laji --- Arbetets art --- Level}}
\put(212,693){\makebox(100,5)[l]{\tiny Aika --- Datum --- Month and year }}
\put(366,693){\makebox(100,5)[l]{\tiny Sivum\"a\"ar\"a --- Sidoantal ---
    Number of pages}}
\put(58,670){\makebox(100,5)[l]{\tiny Tiivistelm\"a --- Referat --- Abstract}}
\put(58,106){\makebox(100,5)[l]{\tiny Avainsanat --- Nyckelord --- Keywords}}
\put(58,83){\makebox(100,5)[l]{\tiny S\"ailytyspaikka --- F\"orvaringsst\"alle
--- Where deposited}}
\put(58,61){\makebox(100,5)[l]{\tiny Muita tietoja --- \"ovriga uppgifter
--- Additional information}}
\put(53,821){\makebox(100,8)[l]{\small HELSINGIN YLIOPISTO --- HELSINGFORS
UNIVERSITET --- UNIVERSITY OF HELSINKI}}
\end{picture}

\pagestyle{empty}
\newpage
}

\endinput
%%
%% End of file `tktltiki.cls'.
