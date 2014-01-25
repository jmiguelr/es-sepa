
es-sepa

Implementacion de la normativa europea SEPA adaptada a España. O al menos a las necesidades de los proyectos con los que estamos trabajando en http://www.virtualsw.com

No es una implementacion completa de la especificacion pero tal vez pueda servir de base para alguien que necesite realizar esta adaptacion. Tal vez nosotros mismos lo extendamos algun dia si tenemos tiempo o necesidad. 

Este codigo fuente se distribuye con licencia Apache 2.0, asi que puede modificarlo o distribuirlo de la forma que quieras.
Aunque no se requiere, se agradeceria que incluyeras este texto donde vayas a usar este software y que nos enviaras un
mensaje para saber que nuestro trabajo ha sido util para ti. Un buen sitio, puede ser en la pagina del repositorio de GitHub
https://github.com/jmiguelr/es-sepa


---------------------------------------------------------------------------

This code is a SEPA implementation adapted to our firm need. Should you find it useful for you.. go ahead and use it. Even more, if you like the arquitecture and want to improve it, better for everyone.

It's not a full SEPA implementation but should be easy enought to extend it to fill all gaps. Perhaps we'll do it in a future

This source code is licensed under Apache 2.0 license schema: you can modify and distribute this piece of software in
any way you want. Altought not needed, we'd be glad to see this text included whenever you use this software and a recognition message to know our work has been useful for you. A good place could be the github page https://github.com/jmiguelr/es-sepa


-----------------------------------------------------------------------------

Note and acknowlegde: Initially based on work from https://github.com/joaoosorio/pt-sepa-iso20022




=====================================================================================================


La implementacion aqui provista utiliza el llamado 'principio de Hollowood' (no nos llames tu, ya te llamaremos nosotros),
una variante del patron de diseño 'inversion de control'.  Aunque al principio pueda parecer una forma de
trabajo un poco desconcertante en la practica reduce significativamente la complejidad del desarrollo ya que
el desarrollador (TU!) solo tiene que proveer los datos que se necesitan. La clase que genera el XML de salida ya
ira llamando a tus clases segun lo necesite.

Para entender el funcionamiento: Tenemos 3 clases (interfaces, en realidad) constructoras de las estructuras
necesarias para generar el xml: SepaFicheroCreator , SepaPagoCreator y SepaOperacionCreator.

Estas estructuras devuelven clases (tambien aqui: interfaces) o Vectores de las clases (interfaces) SepaFichero,
SepaPago y SepaOperacion. Estas clases, desde el punto de vista del interface, simplemente son 'getters' de los datos
necesarios para rellenar las estructuras del XML.  El resto de la implementacion (esto es: de donde saques los
datos para rellenar, si es de base de datos, de ficheros de texto de URLs o de donde sea) es cosa tuya.


Se incluye un test (testBasico.java) a modo de ejemplo: no los tomes como test exhaustivos sino como lo que son: ejemplos.

La llamada minima para generar un documento es de la siguiente forma:

```java

  public void testCreateNewDocument_1() throws Exception {
        String idFicheroAExportar = "MyID";

        SepaFicheroCreator sepaFicheroCreator = new SepaFicheroCreatorTipoTest(idFicheroAExportar);
        SepaPagoCreator sepaPagoCreator = new SepaPagoCreatorTipoTest();
        SepaOperacionCreator sepaOperacionCreator = new SepaOperacionCreatorTipoTest();

        creaDoc(sepaFicheroCreator, sepaPagoCreator, sepaOperacionCreator);
    }

```


