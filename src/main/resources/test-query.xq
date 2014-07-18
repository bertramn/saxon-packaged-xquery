xquery version "1.0" encoding "UTF-8";

(: This is a module import from the classpath as reusable, shared resource :)
import module namespace fext="http://ndc.fleurida.com/test" at "classpath:META-INF/test-function.xqm";

(: make the test output a bit pretty :)
declare namespace saxon = "http://saxon.sf.net/";
declare option saxon:output "indent=yes";

(: Execute this query with the com.fleurida.binda.oxygenxml.demo.DemoPlatformDesignInitializer 
   configured on the saxon engine :)
<test>
  <module-function>{fext:test-module-function('BLA1')}</module-function>
  <module-function>{fext:test-module-function('BLA2')}</module-function>
  <module-function>{fext:test-module-function('BLA3')}</module-function>
  <module-function>{fext:test-module-function('BLA4')}</module-function>
  <extension-function>{fext:test-extension-function('BLA1')}</extension-function>
  <extension-function>{fext:test-extension-function('BLA2')}</extension-function>
  <extension-function>{fext:test-extension-function('BLA3')}</extension-function>
  <extension-function>{fext:test-extension-function('BLA4')}</extension-function>
</test>