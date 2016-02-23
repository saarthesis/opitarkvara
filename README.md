# opitarkvara

## Kuidas jooksutada programmi

1. Laadi alla zip [lingil] (https://www.dropbox.com/s/5tdl4etp8g8dzdd/Opitarkvara_1.0.zip?dl=0)
2. Paki lahti
3. java -jar kasutajaliides.jar

## Kokkuvõtlik logi:

Kuupäev: 2016-02-23

1. Eraldiseisev kaust jooksva programmi jaoks.
2. Haskell ühendatud Java-ga. Saab vajutada "run" ning väljastab tulemuse "puu" tabis.

Kuupäev: 2016-02-13

1. Kasutajaliidese arendus: New/Save/SaveAs/Load teadmusbaas & tabs
2. Mootori arendus: Nüüd on võimalik lisada "not allowed" states ja on võimalik kirjutada kommentaare ja tühje ridu teadmusbaasi. Näide: https://github.com/saarthesis/opitarkvara/blob/master/haskell_solver/teadmusbaas/armukadedad_purjetajad_PROD_with_not_allowed.txt

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


## Kuidas kasutada kasutajaliidest

1. Ole projekti kaustas. /kasutajaliides
2. gradle run

## Kuidas kasutada Solver-it

ghci

Prelude> :l ProductionsSolver.hs

*Main> buildFullTreeFromFile "teadmusbaas/mees_rebane_hani_vili_PROD.txt"

*Main> buildFullTreeFromFile "teadmusbaas/kannud_PROD.txt" 

*Main> buildFullTreeFromFile "teadmusbaas/armukadedad_purjetajad_PROD.txt" 

*Main> buildFullTreeFromFile "teadmusbaas/armukadedad_purjetajad_PROD_not_allowed.txt" 
