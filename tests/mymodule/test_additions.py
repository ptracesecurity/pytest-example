import pytest

from myapp.mymodule.funcs import add


@pytest.mark.parametrize("a, b, c", [(10,20, 30), (20,40,60), (11,22,33)])
def test_add(a, b, c):
    res = add(a, b)
    assert res == c
