#!/usr/bin/python
import networkx as nx
import pygeoip
import sys

def lookUpCountrtryOne (inIP, geoip_path):
    GEOIP = pygeoip.GeoIP(geoip_path, pygeoip.MEMORY_CACHE)
    country = GEOIP.country_name_by_addr(inIP)
    return country

def load_and_display_file(graph_path, geoip_path):
    
    # graph1 = nx.read_graphml('cs327-created-graph.graphml')
    graph1 = nx.read_graphml(graph_path)
    graph1 = graph1.to_undirected()

    all_nodes = list(graph1.nodes())
    print ("Question 1: How many Bitcoin nodes does the Bitcoin network in the data file have?")
    # print ('Nodes: ', all_nodes)
    print ('# of nodes: ', len(all_nodes))

    all_edges = graph1.edges()
    print ("Question 2:  How many edges does the Bitcoin network in the data file have?")
    # print ('Edges: ', all_edges)
    print ('# of edges: ', len(all_edges))

    max_degree = 0
    max_nodes = []
    for node in all_nodes:
        if graph1.degree(node) > max_degree:
            max_degree = graph1.degree(node)
            max_nodes = [node]
        elif graph1.degree(node) == max_degree:
            max_nodes.append(node)


    print ("Question 3: What is the largest node degree? Which nodes have this degree?")
    print ("Max degree: ", max_degree)
    print ("Node(s) with max degree: ", max_nodes)


    min_degree = float('inf')
    min_nodes = []
    for node in all_nodes:
        if graph1.degree(node) < min_degree:
            min_degree = graph1.degree(node)
            min_nodes = [node]
        elif graph1.degree(node) == min_degree:
            min_nodes.append(node)

    print ("Question 4: What is the smallest node degree? Which nodes have this degree?")
    print ("Min degree: ", min_degree)
    print ("Node(s) with min degree: ", min_nodes)
    degree_pairs = list(graph1.degree())
    ranked = sorted(degree_pairs, key=lambda pair: pair[1], reverse=True)
    top_ten = ranked[:10]
    print ("Question 5: Find out the 10 nodes that have the highest degrees. For each node, print out, in the descending order of degrees, the node, its degree and its country.")
    geoip = pygeoip.GeoIP(geoip_path, pygeoip.MEMORY_CACHE)
    for node, degree in top_ten:
        country = geoip.country_name_by_addr(node) or "Unknown"
        print("Node:", node, "Degree:", degree, "Country:", country)

def main():
    if len(sys.argv) != 3:
        print("Usage: python script03.py GRAPH_FILE GEOIP_FILE")
        sys.exit(1)

    graph_path = sys.argv[1]
    geoip_path = sys.argv[2]
    load_and_display_file(graph_path, geoip_path)

if __name__ == "__main__":
    main()
