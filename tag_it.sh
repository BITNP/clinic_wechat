git tag -a v`grep -Po -m1 '[[:digit:]]+\.[[:digit:]]+\.[[:digit:]]+' misc/lightapp.json` -m "auto tag"
