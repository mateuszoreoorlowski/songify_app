SONGIFY: APLIKACJA DO ZARZĄDZANIA ALBUMAMI, ARTYSTAMI I PIOSENKAMI

~~1. można dodać artystę (nazwa artysty)~~
~~2. można dodać gatunek muzyczny (nazwa gatunku)~~
3. można dodać album (tytuł, data wydania, ale musi być w nim przynajmniej jedna piosenka)
~~4. można dodać piosenkę (tytuł, czas trwania, data wydania, język piosenki)~~
5. można dodać artystę od razu z albumem i z piosenką (domyślne wartości)
6. można usunąć artystę (usuwamy wtedy jego piosenki oraz albumy, ale jeśli było więcej niż jeden artysta w albumie, to usuwamy tylko artystę z albumu i samego artystę)
7. można usunąć gatunek muzyczny (ale nie może istnieć piosenka z takim gatunkiem)
8. można usunąć album (ale dopiero wtedy, kiedy nie ma już żadnej piosenki przypisanej do albumu)
9. można usunąć piosenkę, ale nie usuwamy albumu i artystów, gdy była tylko 1 piosenka w albumie
10. można edytować nazwę artysty
11. można edytować nazwę gatunku muzycznego
12. można edytować album (dodawać piosenki, artystów, zmieniać nazwę albumu)
13. można edytować piosenkę (czas trwania, artystę, nazwę piosenki)
14. można przypisać piosenki tylko do albumów
15. można przypisać piosenki do artysty (poprzez album)
16. można przypisać artystów do albumów (album może mieć więcej artystów, artysta może mieć kilka albumów)
17. można przypisać tylko jeden gatunek muzyczny do piosenki
18. gdy nie ma przypisanego gatunku muzycznego do piosenki, to wyświetlamy "default"
~~19. można wyświetlać wszystkie piosenki~~
~~20. można wyświetlać wszystkie gatunki~~
~~21. można wyświetlać wszystkich artystów~~
~~22. można wyświetlać wszystkie albumy~~
23. można wyświetlać konkretne albumy z artystami oraz piosenkami w albumie
24. można wyświetlać konkretne gatunki muzyczne wraz z piosenkami
25. można wyświetlać konkretnych artystów wraz z ich albumami
26. chcemy mieć trwałe dane


HAPPY PATH (user tworzy album "04:01" dla artysty "Kękę" z piosenkami "Mrugnąłem tylko raz", "Tylko ciemność", o gatunku Rap)

given there is no songs, artists, albums and genres created before

. when I go to /song then I can see no songs
2. when I post to /song with Song "Mrugnąłem tylko raz" then Song "Mrugnąłem tylko raz" is returned with id 1
3. when I post to /song with Song "Tylko ciemność" then Song "Tylko ciemność" is returned with id 2
4. when I go to /genre then I can see only default genre with id 1
5. when I post to /genre with Genre "Rap" then Genre "Rap" is returned with id 2
6. when I go to /song/1 then I can see default genre with id 1 and name default
7. when I put to /song/1/genre/1 then Genre with id 2 ("Rap") is added to Song with id 1 ("Mrugnąłem tylko raz")
8. when I go to /song/1 then I can see "Rap" genre
9. when I go to /albums then I can see no albums
10. when I post to /albums with Album "04:01" and Song with id 1 then Album "04:01" is returned with id 1
11. when I go to /albums/1 then I can not see any albums because there is no artist in system
12. when I post to /artists with Artist "Kękę" then Artist "Kękę" is returned with id 1
13. when I put to /artists/1/albums/2 then Artist with id 1 ("Kękę") is added to Album with id 1 ("04:01")
14. when I go to /albums/1 then I can see album with single song with id 1 and single artist with id 1
15. when I put to /albums/1/songs/2 then Song with id 2 ("Tylko ciemność") is added to Album with id 1 ("04:01")
16. when I go to /albums/1 then I can see album with 2 songs (id1 and id2)
