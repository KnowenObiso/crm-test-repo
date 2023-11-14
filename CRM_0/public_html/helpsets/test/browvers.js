//                                browvers.js                                 
//                               Release 1.0.1
//           Copyright © 2002, 2004 Oracle. All rights reserved.      

    var browser=navigator.userAgent.toLowerCase();

    var ie5p    = (browser.indexOf("msie 5.0")!=-1);
    var ie5_5p  = (browser.indexOf("msie 5.5")!=-1);
    var ie6_0p  = (browser.indexOf("msie 6.0")!=-1);

if (ie5_5p || ie5p || ie6_0p) 
  {

//   You can use the following line to test that the JavaScript works. //
//   document.write('<center><font color=red>This browser is IE5.0, IE5.5 or IE6.0</font></center>') //

   document.write('<STYLE>')
   document.write('BODY, P, TABLE, TD, TH, OL, UL, A, DL, DT, DD, BLOCKQUOTE, CAPTION {font-size : x-small;}')
   document.write('TD.zz-nav-header-cell, A.zz-nav-header-link, TD.zz-nav-button-cell, A.zz-nav-button-link {font-size : 90%;}') 
   document.write('</STYLE>')

  }
// -->
