.PHONY: install test basictest

ladytiger/parser.py: grammar.tatsu
	poetry run tatsu --name Lady --outfile ladytiger/parser.py grammar.tatsu
test:
	poetry run pytest
basictest: ladytiger/parser.py
	poetry run py -m ladytiger.parser puzzles/lady1.txt
install:
	poetry install
