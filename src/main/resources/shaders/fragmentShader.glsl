#version 430 core

out vec4 color;

in VS_OUT
{
	vec2 texCoord;
	vec4 color;
} fs_in;

uniform sampler2D texture;

void main()
{
	//color = fs_in.color;
	color = texture(texture, fs_in.texCoord);
}
