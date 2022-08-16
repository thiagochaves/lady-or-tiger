"""
Representation of logical problems.
"""
from __future__ import annotations
from dataclasses import dataclass


def from_spec(spec: str) -> Problem:
    """Creates a problem instance from a specification."""
    return Problem()


class Problem:
    """A logical problem."""

    def __init__(self):
        self.name = "A simple test"
        self.objects = {"kind": ["unique"]}
        self.predicates = ["is_true(kind)"]
        self.rules = [Rule(Predicate("is_true").applied_to("unique"))]


@dataclass
class Rule:
    expression: Expression


class Expression:
    ...

@dataclass
class Predicate(Expression):
    name: str

    def applied_to(self, object: str) -> BoundPredicate:
        return BoundPredicate(self, object)


@dataclass
class BoundPredicate(Expression):
    predicate: Predicate
    object: str
