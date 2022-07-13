.PHONY: install test

ladytiger/parser.py: grammar.tatsu
	poetry run tatsu --name Lady --outfile ladytiger/parser.py grammar.tatsu
test: ladytiger/parser.py
	poetry run py -m ladytiger.parser puzzles/lady1.txt
install:
	poetry install
