import sys

input_data = sys.stdin.read().split()
it = iter(input_data)

t = int(next(it))
out = []

for _ in range(t):
    n = int(next(it))
    adj = [set() for _ in range(n)]
    
    for i in range(n):
        v = int(next(it)) - 1
        adj[i].add(v)
        adj[v].add(i)
        
    visited = [False] * n
    cycles = 0
    paths = 0
    
    for i in range(n):
        if not visited[i]:
            stack = [i]
            visited[i] = True
            is_path = False
            
            while stack:
                u = stack.pop()
                if len(adj[u]) == 1:
                    is_path = True
                    
                for v in adj[u]:
                    if not visited[v]:
                        visited[v] = True
                        stack.append(v)
                        
            if is_path:
                paths += 1
            else:
                cycles += 1
                
    min_ans = cycles + (1 if paths > 0 else 0)
    max_ans = cycles + paths
    out.append(f"{min_ans} {max_ans}")
    
sys.stdout.write('\n'.join(out) + '\n')