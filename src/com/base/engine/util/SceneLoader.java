/*
 * Copyright (C) 2015-2016 CelloCodez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.CapsuleCollider;
import com.base.engine.physics.CubeCollider;
import com.base.engine.physics.MeshCollider;
import com.base.engine.physics.PlaneCollider;
import com.base.engine.physics.SphereCollider;
import com.base.engine.prefabs.DefaultMaterial;
import com.base.engine.prefabs.PlayerComponent;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.meshLoading.OBJModel;

public class SceneLoader {
	
	public static GameObject loadScene(String name) {
		GameObject root = new GameObject("_root");
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(name));
			doc.getDocumentElement().normalize();
			
			NodeList gameObjects = doc.getElementsByTagName("GameObject");
			for (int i = 0; i < gameObjects.getLength(); i++) {
				Node node = gameObjects.item(i);
				
				// Not only check if element node, but also if it is belonging to the main GameObjects list
				// this way, children don't get added twice, once as children, and again as GameObjects inside the root
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getParentNode().getNodeName().equals("GameObjects")) {
					GameObject go = parseGameObject(node);
					root.AddChild(go);
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	private static GameObject parseGameObject(Node node) {
		Element element = (Element) node;
		NodeList moreNodes = element.getChildNodes();
		GameObject go = new GameObject(element.getAttribute("name"));
		for (int i = 0; i < moreNodes.getLength(); i++) {
			Node childNode = moreNodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if (childNode.getNodeName().equals("pos")) {
					go.GetTransform().SetPos(parseVector3f(childNode));
				} else if (childNode.getNodeName().equals("rot")) {
					go.GetTransform().SetRot(new Quaternion(parseVector3f(childNode)));
				} else if (childNode.getNodeName().equals("scale")) {
					go.GetTransform().SetScale(parseVector3f(childNode));
				} else if (childNode.getNodeName().equals("Children")) {
					for (int child = 0; child < childNode.getChildNodes().getLength(); child++) {
						Node childItem = childNode.getChildNodes().item(child);
						if (childItem.getNodeType() == Node.ELEMENT_NODE) {
							GameObject childGO = parseGameObject(childItem);
							go.AddChild(childGO);
						}
					}
				} else if (childNode.getNodeName().equals("Collider")) {
					Element childElement = (Element) childNode;
					if (childElement.getAttribute("type").equals("Plane")) {
						float width = Float.parseFloat(childElement.getAttribute("width"));
						float length = Float.parseFloat(childElement.getAttribute("length"));
						float mass = Float.parseFloat(childElement.getAttribute("mass"));
						PlaneCollider col = new PlaneCollider(mass, width, length);
						go.SetCollider(col);
					} else if (childElement.getAttribute("type").equals("Sphere")) {
						float radius = Float.parseFloat(childElement.getAttribute("radius"));
						float mass = Float.parseFloat(childElement.getAttribute("mass"));
						SphereCollider col = new SphereCollider(mass, radius);
						go.SetCollider(col);
					} else if (childElement.getAttribute("type").equals("Cube")) {
						float width = Float.parseFloat(childElement.getAttribute("width"));
						float height = Float.parseFloat(childElement.getAttribute("height"));
						float length = Float.parseFloat(childElement.getAttribute("length"));
						float mass = Float.parseFloat(childElement.getAttribute("mass"));
						CubeCollider col = new CubeCollider(mass, width, height, length);
						go.SetCollider(col);
					} else if (childElement.getAttribute("type").equals("Capsule")) {
						float radius = Float.parseFloat(childElement.getAttribute("radius"));
						float height = Float.parseFloat(childElement.getAttribute("height"));
						float mass = Float.parseFloat(childElement.getAttribute("mass"));
						CapsuleCollider col = new CapsuleCollider(mass, radius, height);
						go.SetCollider(col);
					} else if (childElement.getAttribute("type").equals("Mesh")) {
						String objFile = childElement.getAttribute("objFile");
						float mass = Float.parseFloat(childElement.getAttribute("mass"));
						MeshCollider col = new MeshCollider(mass, new OBJModel(objFile).ToIndexedModel());
						go.SetCollider(col);
					} else {
						System.err.println("Unknown Collider type: " + childElement.getAttribute("type"));
					}
				} else if (childNode.getNodeName().equals("Components")) {
					for (int component = 0; component < childNode.getChildNodes().getLength(); component++) {
						Node componentNode = childNode.getChildNodes().item(component);
						if (componentNode.getNodeType() == Node.ELEMENT_NODE) {
							Element componentElement = (Element) componentNode;
							if (componentNode.getNodeName().equals("PlayerComponent")) {
								float sens = Float.parseFloat(componentElement.getAttribute("sensitivity"));
								float speed = Float.parseFloat(componentElement.getAttribute("speed"));
								PlayerComponent comp = new PlayerComponent(sens, speed);
								go.AddComponent(comp);
							} else if (componentNode.getNodeName().equals("Camera")) {
								float fovInDegrees = Float.parseFloat(componentElement.getAttribute("fovInDegrees"));
								float zNear = Float.parseFloat(componentElement.getAttribute("zNear"));
								float zFar = Float.parseFloat(componentElement.getAttribute("zFar"));
								Camera comp = new Camera(fovInDegrees, zNear, zFar);
								go.AddComponent(comp);
							} else if (componentNode.getNodeName().equals("MeshRenderer")) {
								@SuppressWarnings("unused")
								String materialName = componentElement.getAttribute("material");
								String objFile = componentElement.getAttribute("objFile");
								Material mat = new DefaultMaterial();
								
								// TODO TODO TODO
								// TODO 				Mess with shader system for multiple materials and shaders
								// TODO TODO TODO
								
								MeshRenderer comp = new MeshRenderer(new Mesh(objFile), mat);
								go.AddComponent(comp);
							} else if (componentNode.getNodeName().equals("DirectionalLight")) {
								float intensity = Float.parseFloat(componentElement.getAttribute("intensity"));
								Vector3f color = new Vector3f(0, 0, 0);
								for (int child = 0; child < componentNode.getChildNodes().getLength(); child++) {
									Node childItem = componentNode.getChildNodes().item(child);
									if (childItem.getNodeType() == Node.ELEMENT_NODE) {
										if (childItem.getNodeName().equals("color")) {
											color = parseVector3f(childItem);
										}
									}
								}
								DirectionalLight comp = new DirectionalLight(color, intensity);
								go.AddComponent(comp);
							} else {
								System.err.println("Unknown GameObject component: " + componentNode.getNodeName());
							}
						}
					}
				} else {
					System.err.println("Unknown GameObject extra tag: " + childNode.getNodeName());
				}
			}
		}
		return go;
	}
	
	private static Vector3f parseVector3f(Node node) {
		Vector3f out = new Vector3f(0, 0, 0);
		Element element = (Element) node;
		out.SetX(Float.parseFloat(element.getAttribute("x")));
		out.SetY(Float.parseFloat(element.getAttribute("y")));
		out.SetZ(Float.parseFloat(element.getAttribute("z")));
		return out;
	}
	
}
