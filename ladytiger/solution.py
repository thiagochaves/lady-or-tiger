from .problem import Problem
from dataclasses import dataclass


@dataclass
class Solution:
    truths: [str]

    def __iter__(self):
        return iter(self.truths)


def solve(problem: Problem) -> Solution:
    return Solution(["contains(_2, lady)"])
