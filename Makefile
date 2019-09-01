all: determinism.db

determinism.db: sql/schema.sql
	@cat $< | sqlite3 $@

clean:
	-rm -fr determinism.db

query:
	 echo -e ".headers on\n.mode column\n.width 40 10 34 10 24\nselect * from det where identity like '%javascript%' order by identity ;" | sqlite3 determinism.db

.PHONY: clean query
