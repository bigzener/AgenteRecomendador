accesorio_de(broca,taladro).
accesorio_de(carda,taladro).
accesorio_de(llave,taladro).
accesorio_de(esmeril,taladro).
accesorio_de(lija,pulidora).
accesorio_de(felpa,pulidora).
accesorio_de(foco,lampara).
accesorio_de(bateria,lampara).
accesorio_de(cargador,lampara).
accesorio_de(segueta,caladora).
accesorio_de(electrodo,soldadora).
accesorio_de(careta,soldadora).
accesorio_de(portaelectrodo,soldadora).

herramienta(X) :- accesorio_de(Y,X).
herramienta(X,Y) :- accesorio_de(Y,X).
accesorio(X) :- accesorio_de(X,Y).

herramientaNecesaria (X) :- accesorio_de(X).