all: determinism.db

determinism.db: sql/schema.sql
	@cat $< | sqlite3 $@

clean:
	-rm -fr determinism.db

query:
	 echo -e '.headers on\n.mode line\n.width 37 20 54 10 30\nselect * from det ;' | sqlite3 determinism.db

.PHONY: clean query
