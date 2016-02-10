import scanner
import sys
data = ""
for line in sys.stdin.readlines():
	data += line

mLexer = scanner.doScan(data)
for tok in mLexer: #iter(mLexer.token, None):
    print repr(tok.type), repr(tok.value)