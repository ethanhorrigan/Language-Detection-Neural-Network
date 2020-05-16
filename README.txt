
Model Training Iterations:
	(1)
	- Vector Size: 1000
	- Hidden Layer, Activation: ReLU, Nodes: 5
	- Hidden Layer, Activation: ReLU, Nodes: 10
	- Average Error per 100 epochs: 0.8495626462371879
	(2)
	- Vector Size: 1000
	- Hidden Layer, Activation: ReLU, Nodes: inputs / 2
	- Hidden Layer, Activation: ReLU, Nodes: inputs / 4
	- Average Error: [0.840981205366104]
	
	
	
	
	
How many hidden neurons?

When choosing an appropriate amount of hidden neurons, I had to be mindful of using too many or not using enough. With too many hidden neurons, it could dramatically increase training time, making it so the I could not train the network in a suitable amount of time. With too little hidden neurons, I could potentially starve the network of possible resources.

Reference: https://books.google.ie/books/about/Practical_Neural_Network_Recipes_in_C++.html?id=7Ez_Pq0sp2EC&redir_esc=y