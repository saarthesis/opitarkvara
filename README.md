# opitarkvara

## Kuidas kasutada

ghci

Prelude> :l ProductionsSolver.hs

*Main> buildFullTreeFromFile "teadmusbaas/mees_rebane_hani_vili_PROD.txt"

*Main> buildFullTreeFromFile "teadmusbaas/kannud_PROD.txt" 

*Main> buildFullTreeFromFile "teadmusbaas/armukadedad_purjetajad_PROD.txt" 


## Kokkuvõtlik logi:

Kuupäev: 2016-01-29

1. 3 ülesande lahendust teadmusbaasi
2. Produktsioonide lahendaja. Suur muudatus. Täiesti teine lähenemine võrreldes varasemaga.

Kuupäev: 2016-01-19

1. Nyyd saab lugeda ylesande pystitust eraldi failist.

Kuupäev: 2016-01-12

Mõtteid:

1. Vb teha Haskellis fullTree ja siis Java-s rakendame erinevaid otsimisalgoritme?
2. Peaks proovima ka teiste ülesanete peal. Praegu on näide "Mees, Rebane, Hani, Vili" .

Protsess:

1. Alguses proovisin Prologis teha "keerdülesande" stiilis. Aga siis sain aru, et see ei ole piisavalt "general" lahendus. Sest peaks tegema igale uuele ülesandele teise Prologi koodi.
2. Nüüd hakkasin arendama Prologis "puu" meetodil, aga arendamise käigus mõtlesin, et on paremaid keeli sellise lähenemise jaoks.
3. Viimase versiooni tegin Haskellis, mis on ka siin üleval.
