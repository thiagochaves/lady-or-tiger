"""
Acceptance tests.
Verify that the system can solve some logical puzzles.
"""
import ladytiger as lt


def test_lady_or_tiger_first_trial():
    problem_spec = load_problem_spec("lady1.txt")
    problem = lt.problem.from_spec(problem_spec)
    solution = lt.solution.solve(problem)
    assert "contains(_2, lady)" in solution


def load_problem_spec(problem_spec_file: str) -> str:
    with open("puzzles/" + problem_spec_file, "r") as file:
        return file.read()
