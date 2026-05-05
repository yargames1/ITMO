size = int(input())
heap = list(map(int, input().split()))

ans = []

def shiftDown(i):
    minIndex = i
    leftChild = 2*i+1
    if leftChild < size and heap[leftChild] < heap[minIndex]:
        minIndex = leftChild
    rightChild = 2*i+2
    if rightChild < size and heap[rightChild] < heap[minIndex]:
        minIndex = rightChild
    if minIndex != i:
        ans.append([i, minIndex])
        heap[i], heap[minIndex] = heap[minIndex], heap[i]
        shiftDown(minIndex)

def buildHeap():
    for i in range(size//2, -1, -1):
        shiftDown(i)

buildHeap()

print(len(ans))
print("\n".join(" ".join(map(str, row)) for row in ans))
