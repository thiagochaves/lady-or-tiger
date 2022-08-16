"""
Acceptance tests.
Verify that the system can solve some logical puzzles.
"""
from ladytiger import problem as ltproblem
from ladytiger import solution as ltsolution


def test_lady_or_tiger_first_trial():
    problem_spec = load_problem_spec("lady1.txt")
    problem = ltproblem.from_spec(problem_spec)
    solution = ltsolution.solve(problem)
    assert "contains(_2, lady)" in solution


def load_problem_spec(problem_spec_file: str) -> str:
    with open("puzzles/" + problem_spec_file, "r") as file:
        return file.read()
