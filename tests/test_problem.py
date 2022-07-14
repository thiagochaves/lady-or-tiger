"""
Tests the creation of problem instances.
"""
import ladytiger.problem as prob


def test_load_simple_problem():
    spec = """name: A simple test
    object kind:
        unique
    predicate is_true(kind)
    rules:
        is_true(unique)"""
    problem = prob.from_spec(spec)
    assert problem.name == "A simple test"
    assert problem.objects == {"kind": ["unique"]}
    assert problem.predicates == ["is_true(kind)"]
    rule = prob.Rule(prob.Predicate("is_true").applied_to("unique"))
    assert problem.rules == [rule]
