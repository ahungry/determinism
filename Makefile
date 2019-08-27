all: determinism.db

determinism.db: sql/schema.sql
	@cat $< | sqlite3 $@

clean:
	-rm -fr determinism.db

.PHONY: clean
