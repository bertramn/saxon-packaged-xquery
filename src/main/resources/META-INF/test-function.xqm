xquery version "1.0" encoding "UTF-8";

module namespace fext = "http://ndc.fleurida.com/test";


(:~
Test function
:)
declare function fext:test-module-function($code as xs:string?)  as xs:string {
  concat('module [', $code, ']: ', fn:current-dateTime())
};